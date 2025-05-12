package com.example.app.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeterinaryResponseDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private double salary;
    private List<VisitResponseDto> visits;
}
