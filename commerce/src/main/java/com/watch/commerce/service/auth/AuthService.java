package com.watch.commerce.service.auth;

import org.springframework.stereotype.Service;

import com.watch.commerce.dto.UserDto;
import com.watch.commerce.request.CreateUserRequest;
import com.watch.commerce.request.RegisterRequest;
import com.watch.commerce.service.user.UserService;

import jakarta.transaction.Transactional;



@Service
public class AuthService implements IAuthService {

    private final UserService userService;

    public AuthService(UserService userService){
        this.userService = userService;
    }
   



    @Transactional
    public UserDto register(RegisterRequest request) {

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("şifreler eşleşmiyor.");
        }

        CreateUserRequest createUserRequest = new CreateUserRequest(); 
        createUserRequest.setFirstname(request.getFirstName());
        createUserRequest.setLastName(request.getLastName());
        createUserRequest.setEmail(request.getEmail());
        createUserRequest.setPassword(request.getPassword());
        return userService.createUser(createUserRequest);
    }



}
