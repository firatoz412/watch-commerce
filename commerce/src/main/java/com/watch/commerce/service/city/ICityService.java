package com.watch.commerce.service.city;
import java.util.List;

import com.watch.commerce.model.City;

public interface ICityService {
  
    List<City> getAllCities();

    City getCityByName(String cityName);

    City getCityById(Long cityId);
    
    
}
