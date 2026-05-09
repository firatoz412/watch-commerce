package com.watch.commerce.service.user;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        return userRepository.findByEmail(email)
        .orElseThrow(()-> new ResourceNotFoundException(email + " email'e sahip bir kullanıcı bulunamadı"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> user.getRole() != null && user != null)
        .map(this::convertToDto)
        .collect(Collectors.toList());
        
    }

    @Override
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException(userId + "'ye sahip bir kullanıcı bulunamadı")
        );
        userRepository.delete(user);

    }

    
    public String generateRandomPhoneNumber() {
        Random random = new Random();
        
        int[] prefixes = {530, 531, 532, 533, 535, 541, 542, 543, 544, 552, 553, 554, 555};
        int prefix = prefixes[random.nextInt(prefixes.length)];
        int remainingDigits = 1000000 + random.nextInt(9000000);
        return "0" + prefix + remainingDigits;
    }

    @Override
    public UserDto createUser(CreateUserRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new AnExistingEmailException("bu e-posta zaten kayıtlı");
        }

        Role role = roleRepository.findByRole("ROLE_USER").orElseThrow(
            () -> new ResourceNotFoundException("rol bulunamadı.")
        );
        String phoneNumber = generateRandomPhoneNumber();

        User user = new User();
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(phoneNumber);
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
