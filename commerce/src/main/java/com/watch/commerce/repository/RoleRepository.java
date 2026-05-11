package com.watch.commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Role;

public interface  RoleRepository extends JpaRepository<Role, Long> {
    

    Optional<Role> findByRole(String role);

    

}
