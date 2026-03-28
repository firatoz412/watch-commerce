package com.watch.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Address;

public interface  AddressRepository extends JpaRepository<Address, Long>{
    
}
