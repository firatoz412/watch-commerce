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
        if (roleRepository.findByRole("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setRole("USER");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByRole("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRole("ADMIN");
            roleRepository.save(adminRole);
        }
        if (userRepository.findByEmail("admin@saatmagasasi.com").isEmpty()) {
            Role adminRole = roleRepository.findByRole("ADMIN").get();
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@saatmagasasi.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
    }
}