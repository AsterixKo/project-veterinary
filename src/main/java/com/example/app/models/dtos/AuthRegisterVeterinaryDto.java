package com.example.app.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegisterVeterinaryDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private double salary;
}
