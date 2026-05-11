package com.watch.commerce.service.auth;

import org.springframework.stereotype.Service;

import com.watch.commerce.dto.UserDto;
import com.watch.commerce.model.City;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.UserRepository;
import com.watch.commerce.request.CreateUserRequest;
import com.watch.commerce.request.RegisterRequest;
import com.watch.commerce.service.city.CityService;
import com.watch.commerce.service.user.UserService;

import jakarta.transaction.Transactional;



@Service
public class AuthService implements IAuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CityService cityService;

    public AuthService(UserService userService,
        UserRepository userRepository,
        CityService cityService){
        this.userService = userService;
        this.userRepository = userRepository;
        this.cityService = cityService;
    }
   



    @Transactional
    public UserDto register(RegisterRequest request) {

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("şifreler eşleşmiyor.");
        }

        CreateUserRequest createUserRequest = new CreateUserRequest();
        City city = cityService.getCityById(request.getCityId());

        createUserRequest.setFirstname(request.getFirstName());
        createUserRequest.setLastName(request.getLastName());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setPassword(request.getPassword());
        createUserRequest.setCityId(city.getId());
        createUserRequest.setAddress(request.getAddress());
        return userService.createUser(createUserRequest);
    }


    public UserDto adminRegister(RegisterRequest request) {
        // Email kontrolü vb. işlemler
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu email zaten kayıtlı!");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Şifreler eşleşmiyor.");
        }

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstname(request.getFirstName());
        createUserRequest.setLastName(request.getLastName());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setPassword(request.getPassword());
        City city = cityService.getCityById(request.getCityId());
        createUserRequest.setCityId(city.getId());
        createUserRequest.setAddress(request.getAddress());
        return userService.createAdmin(createUserRequest);
    }

    public UserDto convertToDto(User user){
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        if(user.getAddress() != null){

            dto.setAddress(user.getAddress().getFullAddress());

            if(user.getAddress().getCity() != null){
                dto.setCity(user.getAddress().getCity().getCityName());
            }
        }

        return dto;
    }



}
