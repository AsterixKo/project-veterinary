package com.example.app.models.dtos;

import com.example.app.models.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitResponseDto {

    private Long id;
    private Date date;
    private Long petId;
    private String description;
    private int duration;
    private Long veterinaryId;
    private VisitStatus visitStatus;
}
