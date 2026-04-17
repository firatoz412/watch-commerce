package com.watch.commerce.service.user;

import java.util.List;

import com.watch.commerce.model.User;


public interface IUserService {
    
    
    User getUser(String email);

    List<User> getAllUser();

    User findByEmail(String email);
}
