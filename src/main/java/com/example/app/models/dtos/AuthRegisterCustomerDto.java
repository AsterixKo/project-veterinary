package com.example.app.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegisterCustomerDto {

    // from Person
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // from Customer
    private String address;
    private String city;
    private String telephone;
}
