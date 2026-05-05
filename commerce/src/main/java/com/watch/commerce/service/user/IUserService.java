package com.watch.commerce.service.user;


import java.util.List;

import com.watch.commerce.model.User;


public interface IUserService {
    
    
    User getUser(String email);

    User findByEmail(String email);

    List<User> getAllUsers();

    

}
