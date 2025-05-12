package com.example.app.controllers;

import com.example.app.exceptions.CustomerHasPetException;
import com.example.app.exceptions.CustomerNotFoundException;
import com.example.app.models.Pet;
import com.example.app.models.dtos.PetCreationDto;
import com.example.app.models.dtos.PetResponseDto;
import com.example.app.models.dtos.VeterinaryResponseDto;
import com.example.app.repositories.PetRepository;
import com.example.app.services.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/pet")
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

    @PostMapping("/add-pet/{idCustomer}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addPet(@PathVariable Long idCustomer, @RequestBody PetCreationDto petCreationDto) {
        try {
            if (petCreationDto.getName() == null || petCreationDto.getName().isEmpty()) {
                return new ResponseEntity<>("name no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (petCreationDto.getBirthDate() == null || petCreationDto.getBirthDate().isEmpty()) {
                return new ResponseEntity<>("birthDate no puede ser vacio", HttpStatus.BAD_REQUEST);
            }

            try {
                // comprobar fecha
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date dateBirth = formatter.parse(petCreationDto.getBirthDate());

                Pet pet = Pet.builder()
                        .name(petCreationDto.getName())
                        .birthDate(dateBirth)
                        .build();

                Pet petSaved = petService.savePet(idCustomer, pet);
                return new ResponseEntity<Pet>(petSaved, HttpStatus.CREATED);
            } catch (ParseException eParse) {
                log.error("Error guardando pet, el formato de fecha no es correcto {}", eParse.getMessage());
                return new ResponseEntity<>("Error al crear el pet, el formato de fecha no es correcto",
                        HttpStatus.BAD_REQUEST);
            } catch (CustomerNotFoundException eCustomerNotFound) {
                log.error("Error guardando pet, el customer no se encuentra en base de datos {}",
                        eCustomerNotFound.getMessage());
                return new ResponseEntity<>("Error guardando pet, el customer no se encuentra en base de datos",
                        HttpStatus.NOT_FOUND);
            } catch (CustomerHasPetException eCustomerHasPetException) {
                log.error("Error guardando pet, el customer ya tiene pet {}",
                        eCustomerHasPetException.getMessage());
                return new ResponseEntity<>("Error guardando pet, el customer ya tiene pet",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error guardando pet {}", e.getMessage());
            return new ResponseEntity<>("Error al crear el pet", HttpStatus.BAD_REQUEST);
        }
    }
}
