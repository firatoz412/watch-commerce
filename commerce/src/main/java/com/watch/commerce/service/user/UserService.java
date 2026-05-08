package com.watch.commerce.service.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.watch.commerce.dto.UserDto;
import com.watch.commerce.exception.AnExistingEmailException;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Role;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.RoleRepository;
import com.watch.commerce.repository.UserRepository;
import com.watch.commerce.request.CreateUserRequest;

@Service
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto createUser(CreateUserRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new AnExistingEmailException("bu e-posta zaten kayıtlı");
        }

        Role role = roleRepository.findByRole("USER").orElseThrow(
            () -> new ResourceNotFoundException("rol bulunamadı.")
        );

        User user = new User();
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);

    }

    public UserDto convertToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        
        if(user.getRole() != null){
            //Role sınıfındaki role alanını set ediyoruz
            userDto.setRoleName(user.getRole().getRole());
        }
        return userDto;
    }

    

    

    
    
}
