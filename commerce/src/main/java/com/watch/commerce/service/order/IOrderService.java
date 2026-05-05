package com.watch.commerce.service.order;

import java.util.List;

import com.watch.commerce.dto.OrderDto;
import com.watch.commerce.model.User;
import com.watch.commerce.request.OrderRequest;
import com.watch.commerce.response.OrderResponse;

public interface  IOrderService {
    

    //user ordersları
    List<OrderDto> getUserOrders(User user);

    //yeni sipariş oluştur
    OrderResponse placeOrder(OrderRequest order,User user);

    //siparişi orderId'ye göre iptal et
    void canceleOrder(Long orderId);

    //order detay sayfası için gerekli
    //OrderDto getOrder(Long orderId);

    
    




}
