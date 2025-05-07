package com.example.app.controllers;

import com.example.app.models.Veterinary;
import com.example.app.models.dtos.AuthRegisterVeterinaryDto;
import com.example.app.services.VeterinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/public/veterinary")
public class VeterinaryController {

    @Autowired
    private VeterinaryService veterinaryService;

    @GetMapping("/profile")
    public String profile(Veterinary veterinary) {
        return "Veterinary profile" + veterinary.getUsername();
    }

    @PostMapping("/add-veterinary")
    public ResponseEntity<?> addVeterinary(@RequestBody AuthRegisterVeterinaryDto authRegisterVeterinaryDto) {

        try {
            if (authRegisterVeterinaryDto.getUsername() == null ||
                    authRegisterVeterinaryDto.getUsername().isEmpty()) {
                return new ResponseEntity<>("username no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterVeterinaryDto.getPassword() == null ||
                    authRegisterVeterinaryDto.getPassword().isEmpty()) {
                return new ResponseEntity<>("password no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterVeterinaryDto.getFirstName() == null ||
                    authRegisterVeterinaryDto.getFirstName().isEmpty()) {
                return new ResponseEntity<>("firstName no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterVeterinaryDto.getLastName() == null ||
                    authRegisterVeterinaryDto.getLastName().isEmpty()) {
                return new ResponseEntity<>("lastName no puede ser vacio", HttpStatus.BAD_REQUEST);
            }

            Veterinary veterinary = veterinaryService.saveUser(Veterinary.builder()
                    .username(authRegisterVeterinaryDto.getUsername())
                    .password(authRegisterVeterinaryDto.getPassword())
                    .firstName(authRegisterVeterinaryDto.getFirstName())
                    .lastName(authRegisterVeterinaryDto.getLastName())
                    .salary(authRegisterVeterinaryDto.getSalary())
                    .build());
            return new ResponseEntity<>(veterinary, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el veterinario", HttpStatus.BAD_REQUEST);
        }


    }
}
