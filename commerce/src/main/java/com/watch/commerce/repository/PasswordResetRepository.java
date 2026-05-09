package com.watch.commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.watch.commerce.model.PasswordResetToken;
import com.watch.commerce.model.User;


public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long>{


    Optional<PasswordResetToken> findByToken(String token);
    @Modifying
    @Transactional
    void deleteByUser(User user);
    
}
