package com.watch.commerce.service.order;

import java.util.List;

import com.watch.commerce.model.Order;
import com.watch.commerce.model.User;
import com.watch.commerce.response.OrderResponse;

public interface  IOrderService {
    

    //user ordersları
    List<Order> getUserOrders(User user);

    //yeni sipariş oluştur
    OrderResponse placeOrder(Order order,User user);

    //siparişi orderId'ye göre iptal et
    void canceleOrder(Long orderId);

    
    




}
