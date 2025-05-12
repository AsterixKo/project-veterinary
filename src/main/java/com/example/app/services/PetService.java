package com.example.app.services;

import com.example.app.exceptions.CustomerHasPetException;
import com.example.app.exceptions.CustomerNotFoundException;
import com.example.app.models.Customer;
import com.example.app.models.Pet;
import com.example.app.models.dtos.PetResponseDto;
import com.example.app.repositories.CustomerRepository;
import com.example.app.repositories.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PetService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PetRepository petRepository;

    public Pet savePet(Long idCustomer, Pet pet) throws CustomerNotFoundException, CustomerHasPetException {

        Optional<Customer> customerOptional = customerRepository.findById(idCustomer);

        if (!customerOptional.isPresent()) {
            log.error("Error customer no encontrado");
            throw new CustomerNotFoundException("Error customer no encontrado");
        }

        if (customerOptional.get().getPet() != null) {
            log.error("Error customer ya tiene una pet");
            throw new CustomerHasPetException("Error customer ya tiene una pet");
        }

        if (pet == null) {
            log.error("Error pet no puede ser null");
            throw new IllegalArgumentException("Error pet no puede ser null");
        }

        if (pet.getName() == null || pet.getName().isEmpty()) {
            log.error("Error name no puede ser vacio");
            throw new IllegalArgumentException("Error name no puede ser vacio");
        }

        if (pet.getBirthDate() == null) {
            log.error("Error birthDate no puede ser null");
            throw new IllegalArgumentException("Error birthDate no puede ser null");
        }

        log.info("guardando pet {}", pet);
        Pet petSaved = petRepository.save(pet);

        Customer customer = customerOptional.get();
        customer.setPet(petSaved);

        log.info("guardando customer {}", customer);
        customerRepository.save(customer);

        return petSaved;
    }

    public List<PetResponseDto> findAll() {

        List<Pet> petList = petRepository.findAll();

        List<PetResponseDto> petResponseDtoList = new ArrayList<>();
        for (Pet pet : petList) {
            PetResponseDto petResponseDto = new PetResponseDto();
            petResponseDto.setId(pet.getId());
            petResponseDto.setName(pet.getName());
            petResponseDto.setBirthDate(pet.getBirthDate());

            petResponseDtoList.add(petResponseDto);
        }

        return petResponseDtoList;
    }
}
