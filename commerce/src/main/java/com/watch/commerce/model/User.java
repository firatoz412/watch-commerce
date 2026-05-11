package com.watch.commerce.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="app_users")
public class User{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    //bir userın birden fazla rolü olabilir
    //mesela admin ve kullanıcı olabilir.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
   
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    private String phone;

    @Column(precision = 10, scale = 2)
    private java.math.BigDecimal balance = BigDecimal.ZERO;

    

}
