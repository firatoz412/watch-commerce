package com.watch.commerce.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.watch.commerce.enums.OrderStatus;
import com.watch.commerce.enums.PaymentMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //birden fazla siparİş bir user'ın olabilir
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull(message="sipariş bir kullanıcıya ait olmalıdır.")
    private User user;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated
    private OrderStatus status;

    @DecimalMin(value="0.0",inclusive=false,message="toplam tutar 0'dan büyük olmalı")
    private BigDecimal totalPrice;

    //teslimat bilgileri
    @NotBlank(message="isim boş bırakılamaz")
    private String firstName;
    
    @NotBlank(message="soyisim boş bırakılamaz")
    private String lastName;

    @NotBlank(message="email boş bırakılamaz")
    private String email;

    @Pattern(regexp = "^(\\d{10,11})$", message = "geçerli bir telefon numarası giriniz")
    @NotBlank(message="telefon boş bırakılamaz")
    private String phone;

    @NotBlank(message = "Adres boş bırakılamaz")
    @Column(length = 500)
    private String address;

    @Enumerated
    private PaymentMethod paymentMethod;
    private String transactionId;



}
