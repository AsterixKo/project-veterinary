package com.example.app.services;


import com.example.app.exceptions.CustomerHasPetException;
import com.example.app.exceptions.CustomerNotFoundException;
import com.example.app.exceptions.RoleNotFoundException;
import com.example.app.exceptions.UserExistsInDatabaseException;
import com.example.app.models.Customer;
import com.example.app.models.Pet;
import com.example.app.models.Veterinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PetServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PetService petService;

    @Test
    @DisplayName("Guardar pet")
    public void savePet() throws UserExistsInDatabaseException, RoleNotFoundException, CustomerHasPetException, CustomerNotFoundException {
        Customer customer = new Customer();
        customer.setUsername("test_customer2");
        customer.setPassword("1234");
        customer.setFirstName("test_first_name");
        customer.setLastName("test_last_name");
        customer.setAddress("test_address");
        customer.setCity("test_city");
        customer.setTelephone("test123456");
        Customer customerSaved = customerService.saveUser(customer);

        //necesitamos customer, debe existir
        //necesitamos el Pet

        Pet pet = new Pet(); // no puede ser null
        pet.setName("test_Lola"); // no puede ser null
        pet.setBirthDate(new Date()); // no puede ser null

        assertThrows(IllegalArgumentException.class, () -> petService.savePet(customerSaved.getId(), null));

        Pet pet1 = new Pet();
        pet1.setName("");
        pet1.setBirthDate(new Date());
        assertThrows(IllegalArgumentException.class,
                () -> petService.savePet(customerSaved.getId(), pet1));

        Pet pet2 = new Pet();
        pet2.setName("test_tor");
        pet2.setBirthDate(null);
        assertThrows(IllegalArgumentException.class, () ->
                petService.savePet(customerSaved.getId(), pet2));

        Pet petSaved = petService.savePet(customerSaved.getId(), pet);

        assertEquals(pet.getName(), petSaved.getName());
        assertEquals(pet.getBirthDate(), petSaved.getBirthDate());

    }
}
