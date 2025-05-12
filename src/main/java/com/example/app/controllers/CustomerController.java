package com.example.app.controllers;

import com.example.app.models.Customer;
import com.example.app.models.dtos.AuthRegisterCustomerDto;
import com.example.app.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Solo los veterinarios pueden crear clientes
    @PostMapping("/add-customer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
