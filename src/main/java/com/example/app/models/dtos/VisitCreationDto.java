package com.example.app.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitCreationDto {

    private String date;
    private String description;
    private int duration;
    private Long idPet;
}
