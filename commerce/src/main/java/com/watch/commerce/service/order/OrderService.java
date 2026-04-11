package com.watch.commerce.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.watch.commerce.enums.OrderStatus;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.Order;
import com.watch.commerce.model.OrderItem;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.OrderRepository;
import com.watch.commerce.service.cart.CartService;

@Service
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository,CartService cartService){
        this.orderRepository = orderRepository;
        this.cartService = cartService;

    }

    @Override
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Override
    public Order createOrder(User user) {
        Cart cart = cartService.getCartByUser(user);
        //kulanıcını sepetini al sipariş boş ise oluşturmas
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.BEKLEMEDE);
        order.setOrderDate(LocalDateTime.now());

        Set<CartItem> items = new HashSet<>();
        BigDecimal totalAmount=BigDecimal.ZERO;

        for(CartItem cartItem : items){

            if(cartItem.getProduct().getStock() < cartItem.getQuantity()){
                throw new ResourceNotFoundException("yetersiz stok");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getUnitPrice());
            orderItem.setOrder(order);
            
            
        // Stoktan düşme
        cartItem.getProduct().setStock(cartItem.getProduct().getStock() - cartItem.getQuantity());

        }
        return orderRepository.save(order);
    }

    @Override
    public void canceleOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.IPTAL_EDILDI);
        orderRepository.save(order);
    }




    







}