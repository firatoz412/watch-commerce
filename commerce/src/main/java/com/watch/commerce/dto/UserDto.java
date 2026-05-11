package com.watch.commerce.dto;

import lombok.Data;


@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String roleName;
    private String city;
    private String address;

}
