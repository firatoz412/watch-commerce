package com.watch.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.watch.commerce.service.userDetails.IUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final IUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
        .requestMatchers("/", "/register", "/login", "/homePage", "/products/**", "/product-detail/**", "/cart/**","/order/**","/search").permitAll()   // /products/ altındaki her şey (detay sayfaları vs.)
        .requestMatchers("/products/**").permitAll() //bütün product controller pathine izin ver
        .requestMatchers("/admin/**").hasRole("USER")
        .anyRequest().authenticated()
         )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .permitAll()
        )
        .rememberMe(remember -> remember
            .userDetailsService(userDetailsService)
            .tokenValiditySeconds(7 * 24 * 60 * 60)
            .key("commerce-watch-secret-key")
        )
        .sessionManagement(session -> session
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        );

    return http.build();
}
}