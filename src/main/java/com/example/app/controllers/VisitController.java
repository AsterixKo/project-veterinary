package com.example.app.controllers;

import com.example.app.models.Pet;
import com.example.app.models.Visit;
import com.example.app.models.dtos.VisitResponseDto;
import com.example.app.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
// Todo: cambiar public
@RequestMapping("/api/public/visit")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PatchMapping("/update-visit-status/{visitId}")
    public ResponseEntity<?> updateVisitStatus(@PathVariable Long visitId, @RequestParam String visitStatus) {
        try {
            log.info("updateVisitStatus visitId {} con visitStatus: {}", visitId, visitStatus);

            VisitResponseDto visitUpdated = visitService.updateVisitStatus(visitId, visitStatus);
            return new ResponseEntity<VisitResponseDto>(visitUpdated, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error actualizando el visitStatus de Visit {}", e.getMessage());
            return new ResponseEntity<>("Error actualizando el visitStatus de Visit", HttpStatus.BAD_REQUEST);
        }
    }
}
