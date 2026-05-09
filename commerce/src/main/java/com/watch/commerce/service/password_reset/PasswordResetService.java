package com.watch.commerce.service.password_reset;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.PasswordResetToken;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.PasswordResetRepository;
import com.watch.commerce.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void createPasswordResetToken(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı."));

        tokenRepository.deleteByUser(user);
        tokenRepository.flush();
        
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(myToken);

        sendEmail(user.getEmail(), token);
    }

    private void sendEmail(String email,String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("firatoz412@gmail.com");
        message.setSubject("Şifre Sıfırlama Talebi");
        message.setText("Şifrenizi sıfırlamak için tıklayın: \n" +
                "http://localhost:9193/auth/reset-password?token=" + token);
        mailSender.send(message);
    }

    @Transactional
    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new RuntimeException("Geçersiz veya süresi dolmuş token."));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.deleteByUser(user); // Güvenlik için token'ı tek kullanımlık yapıyoruz
    }
}