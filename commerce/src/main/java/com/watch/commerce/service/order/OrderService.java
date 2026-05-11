package com.watch.commerce.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.dto.CartItemDto;
import com.watch.commerce.dto.OrderDto;
import com.watch.commerce.dto.OrderItemDto;
import com.watch.commerce.enums.OrderStatus;
import com.watch.commerce.enums.PaymentMethod;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Order;
import com.watch.commerce.model.OrderItem;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.OrderRepository;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.repository.UserRepository;
import com.watch.commerce.request.OrderRequest;
import com.watch.commerce.response.OrderResponse;
import com.watch.commerce.service.cart.CartService;

import jakarta.transaction.Transactional;

@Service
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
        OrderRepository orderRepository,
        CartService cartService,
        ProductRepository productRepository,
        UserRepository userRepository
    ){
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

    }

    @Override
    public List<OrderDto> getUserOrders(User user) {
        if(user == null){
            //return new ArrayList<>();
            return Collections.emptyList();//bellek kulanımı için daha iyi
        }
        List<Order> orders =  orderRepository.findByUserOrderByOrderDateDesc(user);
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderResponse placeOrder(OrderRequest request ,User user) {
        OrderResponse orderResponse = new OrderResponse();
        CartDto cart = cartService.getCartByUser(user);
        
        //sepet boş olması durumunda
        if(cart == null || cart.getItems().isEmpty()){
            orderResponse.setSuccess(false);
            orderResponse.setMessage("sepetiniz boş");
            return orderResponse;
        }

        BigDecimal totalPrice = cart.getTotalPrice();
        BigDecimal userBalance = user.getBalance();

        //bu kontrol = userBalance < totalPrice anlamına geliyor
        if(userBalance.compareTo(totalPrice) <0){
            orderResponse.setSuccess(false);
            orderResponse.setMessage("yeterisiz bakiye.Gerekli toplam miktar " + totalPrice + " mevcut bakiyeniz " + userBalance);
            return orderResponse;
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setFirstName(request.getFirstName());
        order.setLastName(request.getLastName());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.SIPARIS_ONAYLANDI);
        order.setTotalPrice(totalPrice);
        order.setPaymentMethod(PaymentMethod.KREDI_KARTI);
        order.setTransactionId("TXNN-" +System.currentTimeMillis());//sahte id


        try{
            user.setBalance(user.getBalance().subtract(totalPrice));
            userRepository.save(user);
        
            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItemDto cartItem : cart.getItems()){
                Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("")
                );

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(cartItem.getUnitPrice());
                orderItem.setOrder(order);
                
                orderItems.add(orderItem);

            }
            order.setOrderItems(orderItems);
        
    
            Order savedOrder = orderRepository.save(order);
            cartService.clearCart(cart.getId());
            
            orderResponse.setSuccess(true);
            orderResponse.setMessage("sipariş başarıyla oluşturuldu.");
            orderResponse.setOrder(convertToDto(savedOrder));
            
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            orderResponse.setSuccess(false);

            if(e.getMessage().contains("stok yetersiz")){
                orderResponse.setMessage("seçtiğiniz ürünün stoğu yetersiz");
            }else{
                orderResponse.setMessage("sipariş sırasında hata oluştu.");
            }
        }
        
        return orderResponse;
    }

    @Override
    public void canceleOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            ()-> new ResourceNotFoundException("sipariş bulunamadı")
        );
        //sipariş zaten iptal edilmiş ise işlem yapılmasın;
        if (order.getStatus() == OrderStatus.IPTAL_EDILDI) {
            return;
        }

        //sipariş iptal edilirse stoklar iade edilmeli;
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        order.setStatus(OrderStatus.IPTAL_EDILDI);
        orderRepository.save(order);
    }

    //order (entity) -> orderDto
    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setStatus(order.getStatus());
        orderDto.setFirstName(order.getFirstName());
        orderDto.setLastName(order.getLastName());
        orderDto.setEmail(order.getEmail());
        orderDto.setPhone(order.getPhone());
        orderDto.setAddress(order.getAddress());

        List<OrderItemDto> itemDtos = order.getOrderItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPriceAtPurchase(item.getPriceAtPurchase());
            BigDecimal total = item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity()));
            itemDto.setTotalPrice(total);
            return itemDto;
        }).collect(Collectors.toList());

        orderDto.setOrderItems(itemDtos);
        return orderDto;
    }




    







}