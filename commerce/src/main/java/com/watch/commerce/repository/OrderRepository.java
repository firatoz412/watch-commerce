package com.watch.commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Order;
import com.watch.commerce.model.User;


public interface OrderRepository extends JpaRepository<Order, Long>{


    List<Order> findByUserOrderByOrderDateDesc(User user);
    






}