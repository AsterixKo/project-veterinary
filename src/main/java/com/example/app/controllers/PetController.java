package com.example.app.controllers;

import com.example.app.models.dtos.PetResponseDto;
import com.example.app.models.dtos.VeterinaryResponseDto;
import com.example.app.repositories.PetRepository;
import com.example.app.services.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
// Todo: cambiar ruta public
@RequestMapping("/api/public/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/get-pets")
    public ResponseEntity<?> getAllPets() {
        try{
            List<PetResponseDto> petResponseDtoList = petService.findAll();
            return new ResponseEntity<List<PetResponseDto>>(petResponseDtoList, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("Error obteniendo pets {}", e.getMessage());
            return new ResponseEntity<>("Error obteniendo pets", HttpStatus.BAD_REQUEST);
        }
    }
}
