package com.watch.commerce.service.role;

import org.springframework.stereotype.Service;

import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Role;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String roleName){
        return roleRepository.findByRole(roleName).orElseThrow(
            ()-> new ResourceNotFoundException("userın rolü bulunamadı.")
        );
    }

    public void assignRoleToUser(User user, String roleName) {
        Role role = getRoleByName(roleName);
        user.setRole(role);
    }
}
