package com.example.app.services;

import com.example.app.models.Person;
import com.example.app.models.Veterinary;
import com.example.app.repositories.PersonRepository;
import com.example.app.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    // Method to check if the password is correct
    // It compares the raw password with the encrypted password in the database
    public boolean checkPassword(Person person, String password) {
        return passwordEncoder.matches(password, person.getPassword());
    }
}
