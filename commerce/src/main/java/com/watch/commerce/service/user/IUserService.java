package com.watch.commerce.service.user;


import java.util.List;

import com.watch.commerce.dto.UserDto;
import com.watch.commerce.model.User;
import com.watch.commerce.request.CreateUserRequest;


public interface IUserService {
    
    
    User getUser(String email);

    User findByEmail(String email);

    List<User> getAllUsers();

    UserDto createUser(CreateUserRequest request);

    

}
