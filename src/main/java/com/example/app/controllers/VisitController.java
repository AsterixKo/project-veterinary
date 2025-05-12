package com.example.app.controllers;

import com.example.app.exceptions.PetNotFoundException;
import com.example.app.exceptions.VeterinaryNotFoundException;
import com.example.app.models.Pet;
import com.example.app.models.Visit;
import com.example.app.models.dtos.VisitCreationDto;
import com.example.app.models.dtos.VisitResponseDto;
import com.example.app.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/visit")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PatchMapping("/update-visit-status/{visitId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PostMapping("/add-visit/{idVeterinary}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addVisit(@PathVariable Long idVeterinary, @RequestBody VisitCreationDto visitCreationDto) {
        try {
            if (visitCreationDto.getDuration() <= 0) {
                return new ResponseEntity<>("duration no puede ser menor o igual a 0",
                        HttpStatus.BAD_REQUEST);
            } else if (visitCreationDto.getDescription() == null || visitCreationDto.getDescription().isEmpty()) {
                return new ResponseEntity<>("description no puede ser vacio",
                        HttpStatus.BAD_REQUEST);
            } else if (visitCreationDto.getIdPet() == null) {
                return new ResponseEntity<>("idPet no puede ser null", HttpStatus.BAD_REQUEST);
            }

            // validar fecha con tiempo
            String formatDateTime = "dd-MM-yyyy HH:mm:ss";
            try {
                // comprobar fecha

                SimpleDateFormat formatter = new SimpleDateFormat(formatDateTime, Locale.ENGLISH);
                Date dateVisit = formatter.parse(visitCreationDto.getDate());

                Visit visit = Visit.builder()
                        .date(dateVisit)
                        .description(visitCreationDto.getDescription())
                        .duration(visitCreationDto.getDuration())
                        .build();

                VisitResponseDto visitResponseDto = visitService.saveNewVisit(
                        idVeterinary, visitCreationDto.getIdPet(), visit);
                return new ResponseEntity<VisitResponseDto>(visitResponseDto, HttpStatus.CREATED);
            } catch (ParseException eParse) {
                log.error("Error guardando Visit, el formato de fecha no es correcto {}", eParse.getMessage());
                return new ResponseEntity<>("Error al crear el Visit, el formato de fecha no es correcto "
                        + formatDateTime,
                        HttpStatus.BAD_REQUEST);
            } catch (VeterinaryNotFoundException eVeterinary) {
                log.error("Error guardando Visit, veterinario no encontrado {}", eVeterinary.getMessage());
                return new ResponseEntity<>("Error al crear el Visit, veterinario no encontrado",
                        HttpStatus.NOT_FOUND);
            } catch (PetNotFoundException ePet) {
                log.error("Error guardando Visit, pet no encontrado {}", ePet.getMessage());
                return new ResponseEntity<>("Error al crear el Visit, pet no encontrado",
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error creando Visit {}", e.getMessage());
            return new ResponseEntity<>("Error creando Visit", HttpStatus.BAD_REQUEST);
        }
    }
}
