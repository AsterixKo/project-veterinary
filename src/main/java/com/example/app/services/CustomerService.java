package com.example.app.services;

import com.example.app.exceptions.RoleNotFoundException;
import com.example.app.exceptions.UserExistsInDatabaseException;
import com.example.app.models.Customer;
import com.example.app.models.ERole;
import com.example.app.models.Role;
import com.example.app.repositories.CustomerRepository;
import com.example.app.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer saveUser(Customer customer) throws UserExistsInDatabaseException, RoleNotFoundException {

        Optional<Customer> customerOptional = customerRepository.findByUsername(customer.getUsername());

        if(customerOptional.isPresent()){
            log.error("El usuario ya existe en la base de datos");
            throw new UserExistsInDatabaseException("El usuario ya existe en la base de datos");
        }

        if(customer.getUsername() == null || customer.getPassword().isEmpty()){
            log.error("Error username no puede ser vacio");
            throw new IllegalArgumentException("Error username no puede ser vacio");
        }

        if(customer.getFirstName() == null || customer.getFirstName().isEmpty()) {
            log.error("Error el firstName no puede ser vacio");
            throw new IllegalArgumentException("Error el firstName no puede ser vacio");
        }

        if(customer.getLastName() == null || customer.getLastName().isEmpty()){
            log.error("Error el lastName no puede ser vacio");
            throw new IllegalArgumentException("Error el lastName no puede ser vacio");
        }

        if(customer.getCity() == null || customer.getCity().isEmpty()){
            log.error("Error el city no puede ser vacio");
            throw new IllegalArgumentException("Error el city no puede ser vacio");
        }

        if(customer.getAddress() == null || customer.getAddress().isEmpty()){
            log.error("Error el address no puede ser vacio");
            throw new IllegalArgumentException("Error el address no puede ser vacio");
        }

        if(customer.getTelephone() == null || customer.getTelephone().isEmpty()){
            log.error("Error el telephone no puede ser vacio");
            throw new IllegalArgumentException("Error el telephone no puede ser vacio");
        }

        Optional<Role> roleOptional = roleRepository.findByName(ERole.ROLE_USER);

        if(!roleOptional.isPresent()) {
            log.error("El rol no existe en base de datos");
            throw new RoleNotFoundException("El rol no existe en base de datos");
        }

        customer.setRoles(Arrays.asList(roleOptional.get()));


        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        // el encode convierte la string en algo parecido a esto $2a$10$Fj1RI/RDkPj2wShDY5syye2FvOdCiUx41DE.gV0UoxIZjwb.XhV9u
        return customerRepository.save(customer);
    }

    // Method to check if the password is correct
    // It compares the raw password with the encrypted password in the database
    public boolean checkPassword(Customer customer, String password) {
        return passwordEncoder.matches(password, customer.getPassword());
    }

    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
}
