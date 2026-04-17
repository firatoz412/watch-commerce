package com.watch.commerce.service.user;

import org.springframework.stereotype.Service;

import com.watch.commerce.model.User;
import com.watch.commerce.repository.UserRepository;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    
    
}
