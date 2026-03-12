package com.watch.commerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    
    //bir user birden fazla adrese sahip olabilir
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    //bir şehirde birden fazla adres olabilir
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private String fullAddress;




}
