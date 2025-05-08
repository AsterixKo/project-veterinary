package com.example.app.services;

import com.example.app.exceptions.RoleNotFoundException;
import com.example.app.exceptions.UserExistsInDatabaseException;
import com.example.app.models.Veterinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VeterinaryServiceTest {
    private Veterinary veterinary;

    @Autowired
    VeterinaryService userService;


    @BeforeEach
    public void setUp() throws UserExistsInDatabaseException, RoleNotFoundException {
        veterinary = new Veterinary();
        veterinary.setUsername("testing_jon_admin");
        veterinary.setPassword("1234");
        veterinary.setFirstName("Jon");
        veterinary.setLastName("Doe");
        System.out.println("El usuario inicial es: " + veterinary);

        userService.saveUser(veterinary);
    }

//    @AfterEach
//    public void tearDown() {
//        userRepository.delete(user);
//    }

    @Test
    @DisplayName("La encriptación de passwords es correcta")
    public void passwordEncryption() {
        assertTrue(veterinary.getPassword().startsWith("$2a$")); // todas las strings encriptadas con bcrypt usando el algoritmo que estamos usando deberían empezar así
        System.out.println("este es el user final: " + veterinary);
    }
}
