package com.example.app.services;

import com.example.app.exceptions.*;
import com.example.app.models.Customer;
import com.example.app.models.Pet;
import com.example.app.models.Veterinary;
import com.example.app.models.Visit;
import com.example.app.models.dtos.VisitResponseDto;
import com.example.app.repositories.PetRepository;
import com.example.app.repositories.VeterinaryRepository;
import com.example.app.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VisitServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private VeterinaryService veterinaryService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private CustomerService customerService;

    @Test
    @DisplayName("Guardando nueva visita")
    public void saveNewVisit() throws UserExistsInDatabaseException,
            RoleNotFoundException, CustomerHasPetException, CustomerNotFoundException,
            VeterinaryNotFoundException, PetNotFoundException {

        Veterinary veterinary = new Veterinary();
        veterinary.setUsername("test_visit_vet1");
        veterinary.setPassword("1234");
        veterinary.setFirstName("test_firstName");
        veterinary.setLastName("test_lastName");
        veterinary.setSalary(20000);

        Veterinary veterinarySaved = veterinaryService.saveUser(veterinary);

        Customer customer = new Customer();
        customer.setUsername("test_visit_customer1");
        customer.setPassword("1234");
        customer.setFirstName("test_first_name");
        customer.setLastName("test_last_name");
        customer.setAddress("test_address");
        customer.setCity("test_city");
        customer.setTelephone("test123456");
        Customer customerSaved = customerService.saveUser(customer);

        Pet pet = new Pet();
        pet.setName("test_pet1");
        pet.setBirthDate(new Date());

        Pet petSaved = petService.savePet(customerSaved.getId(), pet);

        // idVeterinary no puede ser null
        // idPet no puede ser null
        // visit no puede ser null

        assertThrows(IllegalArgumentException.class, () -> visitService.saveNewVisit(
                null, null, null
        ));

        assertThrows(IllegalArgumentException.class, () -> visitService.saveNewVisit(
                veterinarySaved.getId(), null, null
        ));

        assertThrows(IllegalArgumentException.class, () -> visitService.saveNewVisit(
                veterinarySaved.getId(), petSaved.getId(), null
        ));

        Visit visit = new Visit();
        visit.setDate(null); // no puede ser null
        visit.setDescription("pata rota");

        assertThrows(IllegalArgumentException.class, () -> visitService.saveNewVisit(
                veterinarySaved.getId(), petSaved.getId(), visit
        ));

        Visit visit1 = new Visit();
        visit1.setDate(new Date());
        visit1.setDescription("patilla rota");
        visit.setDuration(-1);// debe ser mayor a cero

        assertThrows(IllegalArgumentException.class, () -> visitService.saveNewVisit(
                veterinarySaved.getId(), petSaved.getId(), visit1
        ));

        Visit visit2 = new Visit();
        visit2.setDate(new Date());
        visit2.setDescription("dolor de tripa");
        visit2.setDuration(100);

        // TODO: Me lanza el siguiente error, tratar de solucionarlo:
        // org.springframework.dao.InvalidDataAccessApiUsageException: detached entity passed to persist: com.example.app.models.Pet
        VisitResponseDto visitResponseDto = visitService.saveNewVisit(
                veterinarySaved.getId(), petSaved.getId(), visit2);

        assertEquals(visit2.getDescription(), visitResponseDto.getDescription());
        assertEquals(visit2.getDuration(), visitResponseDto.getDuration());
    }
}
