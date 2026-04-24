package com.watch.commerce.service.order;

import java.util.List;

import com.watch.commerce.model.Order;
import com.watch.commerce.model.User;

public interface  IOrderService {
    

    //user ordersları
    List<Order> getUserOrders(User user);

    //yeni sipariş oluştur
    Order placeOrder(User user, String firstName, String lastName, String email, 
                      String phone, String address, String paymentMethod);

    //siparişi orderId'ye göre iptal et
    void canceleOrder(Long orderId);

    
    




}
