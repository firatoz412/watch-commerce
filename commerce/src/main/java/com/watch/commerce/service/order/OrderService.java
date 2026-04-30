package com.watch.commerce.service.order;

import java.math.BigDecimal;
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
import com.watch.commerce.repository.UserRepository;
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
    public List<Order> getUserOrders(User user) {
        if(user == null){
            //return new ArrayList<>();
            return Collections.emptyList();//bellek kulanımı için daha iyi
        }
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Transactional
    @Override
    public OrderResponse placeOrder(Order order ,User user) {
        OrderResponse orderResponse = new OrderResponse();
        Cart cart = cartService.getCartByUser(user);
        
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

        user.setBalance(user.getBalance().subtract(totalPrice));
        userRepository.save(user);
        //sepet boş değilse yeni order oluşturalım;
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());//oluşturulma zamanını al
        order.setStatus(OrderStatus.TESLIM_EDILDI);
        order.setTotalPrice(cart.getTotalPrice());

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
        cartService.clearCart(cart.getId());
        orderRepository.save(order);
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




    







}