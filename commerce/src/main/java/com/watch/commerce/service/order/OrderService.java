package com.watch.commerce.service.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.watch.commerce.enums.OrderStatus;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.Order;
import com.watch.commerce.model.OrderItem;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.OrderRepository;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.service.cart.CartService;

import jakarta.transaction.Transactional;

@Service
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderService(
        OrderRepository orderRepository,
        CartService cartService,
        ProductRepository productRepository
    ){
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;

    }

    @Override
    public List<Order> getUserOrders(User user) {
        if(user == null){
            //return new ArrayList<>();
            return Collections.emptyList();//bellek kulanımı için daha iyi
        }
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Transactional
    @Override
    public Order placeOrder(User user,String firstName,String lastName,String email,
        String phone,String address,String paymentMethod
    ) {
        Cart cart = cartService.getCartByUser(user);
        
        if(cart == null || cart.getItems().isEmpty()){
            throw new RuntimeException("sepet boş,sipariş oluşturulamaz.");
        }
        //sepet boş değilse yeni order oluşturalım;
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());//oluşturulma zamanını al
        order.setStatus(OrderStatus.BEKLEMEDE);
        order.setTotalPrice(cart.getTotalPrice());

        //formdan gelen bilgileri ayarlayalım;
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getItems()){
            Product product = cartItem.getProduct();
            //veri tabanında gerçek stoku görmek için cartItem yerine product ile kontrol ediyoruz
            if(product.getStock() < cartItem.getQuantity()){
                throw new ResourceNotFoundException("yetersiz stok");
            }

            //ürün stoğunu azaltalım;
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getUnitPrice());
            orderItem.setOrder(order);
            
            orderItems.add(orderItem);

        }
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return orderRepository.save(savedOrder);
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




    







}