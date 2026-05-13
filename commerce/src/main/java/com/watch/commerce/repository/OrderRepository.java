package com.watch.commerce.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.watch.commerce.model.Order;
import com.watch.commerce.model.User;


public interface OrderRepository extends JpaRepository<Order, Long>{


    List<Order> findByUserOrderByOrderDateDesc(User user);

    @Query("SELECT SUM(o.totalPrice) FROM Order o")//spring bu metodu hazır bilmez query yazmamız gerekir.
    BigDecimal calculateTotalRevenue();



}