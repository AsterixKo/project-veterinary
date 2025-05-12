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
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
// Todo: inicialmente a public para poder crear el primer veterinary,
//  luego se debe cambiar a admin para restringir el acceso a para solo los ROLE_ADMIN
@RequestMapping("/api/public/veterinary")
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

    @PostMapping("/add-customer")
    public ResponseEntity<?> addCustomer(@RequestBody AuthRegisterCustomerDto authRegisterCustomerDto) {

        try {
            if (authRegisterCustomerDto.getUsername() == null ||
                    authRegisterCustomerDto.getUsername().isEmpty()) {
                return new ResponseEntity<>("username no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterCustomerDto.getPassword() == null ||
                    authRegisterCustomerDto.getPassword().isEmpty()) {
                return new ResponseEntity<>("password no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterCustomerDto.getFirstName() == null ||
                    authRegisterCustomerDto.getFirstName().isEmpty()) {
                return new ResponseEntity<>("firstName no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterCustomerDto.getLastName() == null ||
                    authRegisterCustomerDto.getLastName().isEmpty()) {
                return new ResponseEntity<>("lastName no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterCustomerDto.getAddress() == null ||
                    authRegisterCustomerDto.getAddress().isEmpty()) {
                return new ResponseEntity<>("address no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterCustomerDto.getCity() == null ||
                    authRegisterCustomerDto.getCity().isEmpty()) {
                return new ResponseEntity<>("city no puede ser vacio", HttpStatus.BAD_REQUEST);
            } else if (authRegisterCustomerDto.getTelephone() == null ||
                    authRegisterCustomerDto.getTelephone().isEmpty()) {
                return new ResponseEntity<>("telephone no puede ser vacio", HttpStatus.BAD_REQUEST);
            }

            log.info("vamos a guardar el customer");
            Customer customer = customerService.saveUser(Customer.builder()
                    .username(authRegisterCustomerDto.getUsername())
                    .password(authRegisterCustomerDto.getPassword())
                    .firstName(authRegisterCustomerDto.getFirstName())
                    .lastName(authRegisterCustomerDto.getLastName())
                    .city(authRegisterCustomerDto.getCity())
                    .address(authRegisterCustomerDto.getAddress())
                    .telephone(authRegisterCustomerDto.getTelephone())
                    .build());
            return new ResponseEntity<>(customer, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error guardando customer {}", e.getMessage());
            return new ResponseEntity<>("Error al crear el customer", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-pet/{idCustomer}")
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

    @PostMapping("/add-visit/{idVeterinary}")
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

                Visit visitSaved = visitService.saveNewVisit(idVeterinary, visitCreationDto.getIdPet(), visit);
                return new ResponseEntity<Visit>(visitSaved, HttpStatus.CREATED);
            } catch (ParseException eParse) {
                log.error("Error guardando Visit, el formato de fecha no es correcto {}", eParse.getMessage());
                return new ResponseEntity<>("Error al crear el Visit, el formato de fecha no es correcto " + formatDateTime,
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
}
