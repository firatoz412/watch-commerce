package com.watch.commerce.service.city;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.City;
import com.watch.commerce.repository.CityRepository;

@Service
public class CityService implements ICityService{

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository){
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getAllCities() {
       return cityRepository.findAll();
    }

    @Override
    public City getCityByName(String cityName) {
        return cityRepository.findByCityName(cityName).orElseThrow(
            () -> new ResourceNotFoundException("şehir adı bulunamad")
        );
    }

    @Override
    public City getCityById(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(
            () -> new ResourceNotFoundException("şehir bulunamadı")
        );
    }  
    
}
