package com.watch.commerce.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.watch.commerce.model.Role;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.RoleRepository;
import com.watch.commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (roleRepository.findByRole("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setRole("ROLE_USER");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByRole("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRole("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
        if (userRepository.findByEmail("admin@saatmagasasi.com").isEmpty()) {
            Role adminRole = roleRepository.findByRole("ROLE_ADMIN").get();
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@saatmagasasi.com");
            admin.setPassword(passwordEncoder.encode("12345678"));
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
    }
}