package com.watch.commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByCityName(String cityName);
    
}
