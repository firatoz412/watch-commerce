package com.watch.commerce.service.userDetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService{
    
    UserDetails loadUserByUsername(String username);


}