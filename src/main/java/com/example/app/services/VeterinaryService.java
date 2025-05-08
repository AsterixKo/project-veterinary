package com.example.app.services;

import com.example.app.exceptions.RoleNotFound;
import com.example.app.exceptions.UserExistsInDatabase;
import com.example.app.models.ERole;
import com.example.app.models.Role;
import com.example.app.models.Veterinary;
import com.example.app.repositories.RoleRepository;
import com.example.app.repositories.VeterinaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class VeterinaryService {
    @Autowired
    private VeterinaryRepository veterinaryRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Veterinary saveUser(Veterinary veterinary) throws UserExistsInDatabase, RoleNotFound {
        Optional<Veterinary> veterinaryOptional = veterinaryRepository.findByUsername(veterinary.getUsername());

        if(veterinaryOptional.isPresent()){
            throw new UserExistsInDatabase("El usuario ya existe en la base de datos");
        }

        // comprobacion de campos
        if(veterinary.getUsername() == null || veterinary.getPassword().isEmpty()){
            log.error("Error username no puede ser vacio");
            throw new IllegalArgumentException("Error username no puede ser vacio");
        }

        if(veterinary.getFirstName() == null || veterinary.getFirstName().isEmpty()) {
            log.error("Error el firstName no puede ser vacio");
            throw new IllegalArgumentException("Error el firstName no puede ser vacio");
        }

        if(veterinary.getLastName() == null || veterinary.getLastName().isEmpty()){
            log.error("Error el lastName no puede ser vacio");
            throw new IllegalArgumentException("Error el lastName no puede ser vacio");
        }

        Optional<Role> roleOptional = roleRepository.findByName(ERole.ROLE_ADMIN);

        if(!roleOptional.isPresent()) {
            log.error("El rol no existe en base de datos");
            throw new RoleNotFound("El rol no existe en base de datos");
        }

        veterinary.setRoles(Arrays.asList(roleOptional.get()));


        veterinary.setPassword(passwordEncoder.encode(veterinary.getPassword()));
        // el encode convierte la string en algo parecido a esto $2a$10$Fj1RI/RDkPj2wShDY5syye2FvOdCiUx41DE.gV0UoxIZjwb.XhV9u
        return veterinaryRepository.save(veterinary);
    }

    // Method to check if the password is correct
    // It compares the raw password with the encrypted password in the database
    public boolean checkPassword(Veterinary veterinary, String password) {
        return passwordEncoder.matches(password, veterinary.getPassword());
    }

    public Optional<Veterinary> findByUsername(String username) {
        return veterinaryRepository.findByUsername(username);
    }
}
