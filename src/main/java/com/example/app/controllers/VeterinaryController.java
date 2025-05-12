package com.example.app.controllers;

import com.example.app.exceptions.CustomerHasPetException;
import com.example.app.exceptions.CustomerNotFoundException;
import com.example.app.exceptions.PetNotFoundException;
import com.example.app.exceptions.VeterinaryNotFoundException;
import com.example.app.models.Customer;
import com.example.app.models.Pet;
import com.example.app.models.Veterinary;
import com.example.app.models.Visit;
import com.example.app.models.dtos.*;
import com.example.app.services.CustomerService;
import com.example.app.services.PetService;
import com.example.app.services.VeterinaryService;
import com.example.app.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/veterinary")
public class VeterinaryController {

    @Autowired
    private VeterinaryService veterinaryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PetService petService;

    @Autowired
    private VisitService visitService;

    @GetMapping("/profile")
    public String profile(Veterinary veterinary) {
        return "Veterinary profile" + veterinary.getUsername();
    }

    // TODO: INICIALMENTE PARA CREAR EL PRIMER VETERINARY QUITAR LA ANOTACION PreAuthorize
    @PostMapping("/add-veterinary")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

            log.info("Vamos a guardar el Veterinary");
            Veterinary veterinary = veterinaryService.saveUser(Veterinary.builder()
                    .username(authRegisterVeterinaryDto.getUsername())
                    .password(authRegisterVeterinaryDto.getPassword())
                    .firstName(authRegisterVeterinaryDto.getFirstName())
                    .lastName(authRegisterVeterinaryDto.getLastName())
                    .salary(authRegisterVeterinaryDto.getSalary())
                    .build());
            return new ResponseEntity<>(veterinary, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error creando el veterinary {}", e.getMessage());
            return new ResponseEntity<>("Error al crear el veterinario", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{veterinaryId}")
    public ResponseEntity<?> getVeterinaryById(@PathVariable Long veterinaryId) {
        try {
            log.info("getVeterinaryById {}", veterinaryId);
            VeterinaryResponseDto veterinaryResponseDto = veterinaryService.findById(veterinaryId);
            return new ResponseEntity<VeterinaryResponseDto>(veterinaryResponseDto, HttpStatus.FOUND);
        } catch (VeterinaryNotFoundException eVeterinary) {
            log.error("Error veterinario no encontrado {}", eVeterinary.getMessage());
            return new ResponseEntity<>("Error veterinario no encontrado",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error obteniendo Veterinary {}", e.getMessage());
            return new ResponseEntity<>("Error obteniendo Veterinary", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-veterinaries")
    public ResponseEntity<?> getVeterinaries() {
        try{
            List<VeterinaryResponseDto> veterinaryResponseDtoList = veterinaryService.findAll();
            return new ResponseEntity<List<VeterinaryResponseDto>>(veterinaryResponseDtoList, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("Error obteniendo veterinarios {}", e.getMessage());
            return new ResponseEntity<>("Error obteniendo veterinarios", HttpStatus.BAD_REQUEST);
        }
    }
}
