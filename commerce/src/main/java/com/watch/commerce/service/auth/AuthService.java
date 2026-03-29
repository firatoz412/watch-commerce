package com.watch.commerce.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.watch.commerce.exception.AnExistingEmailException;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Role;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.RoleRepository;
import com.watch.commerce.repository.UserRepository;
import com.watch.commerce.request.RegisterRequest;

import jakarta.transaction.Transactional;



@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
   



    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AnExistingEmailException("Bu e-posta zaten kayıtlı.");
        }

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("şifreler eşleşmiyor.");
        }

        Role role = roleRepository.findByRole("USER")
            .orElseThrow(() -> new ResourceNotFoundException("Rol bulunamadı."));

        User user = new User();//yeni kullanıcı oluştur ve bilgileri set ile ayarla
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);
    }



}
