package com.example.app.controllers;

import com.example.app.models.Customer;
import com.example.app.models.Veterinary;
import com.example.app.models.dtos.AuthRegisterCustomerDto;
import com.example.app.models.dtos.AuthRegisterVeterinaryDto;
import com.example.app.services.CustomerService;
import com.example.app.services.VeterinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api/public/veterinary")
public class VeterinaryController {

    @Autowired
    private VeterinaryService veterinaryService;

    @Autowired
    private CustomerService customerService;

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
}
