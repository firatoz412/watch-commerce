package com.watch.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Order;
public interface OrderRepository extends JpaRepository<Order, Long>{



}