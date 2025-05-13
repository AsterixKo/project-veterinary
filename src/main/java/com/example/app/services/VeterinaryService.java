package com.example.app.services;

import com.example.app.exceptions.RoleNotFoundException;
import com.example.app.exceptions.UserExistsInDatabaseException;
import com.example.app.exceptions.VeterinaryNotFoundException;
import com.example.app.models.*;
import com.example.app.models.dtos.VeterinaryResponseDto;
import com.example.app.models.dtos.VisitResponseDto;
import com.example.app.repositories.RoleRepository;
import com.example.app.repositories.VeterinaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class VeterinaryService {
    @Autowired
    private VeterinaryRepository veterinaryRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Veterinary saveUser(Veterinary veterinary) throws UserExistsInDatabaseException, RoleNotFoundException {
        Optional<Veterinary> veterinaryOptional = veterinaryRepository.findByUsername(veterinary.getUsername());

        if (veterinaryOptional.isPresent()) {
            throw new UserExistsInDatabaseException("El usuario ya existe en la base de datos");
        }

        // comprobacion de campos
        if (veterinary.getUsername() == null || veterinary.getUsername().isEmpty()) {
            log.error("Error username no puede ser vacio");
            throw new IllegalArgumentException("Error username no puede ser vacio");
        }

        if (veterinary.getFirstName() == null || veterinary.getFirstName().isEmpty()) {
            log.error("Error el firstName no puede ser vacio");
            throw new IllegalArgumentException("Error el firstName no puede ser vacio");
        }

        if (veterinary.getLastName() == null || veterinary.getLastName().isEmpty()) {
            log.error("Error el lastName no puede ser vacio");
            throw new IllegalArgumentException("Error el lastName no puede ser vacio");
        }

        Optional<Role> roleOptional = roleRepository.findByName(ERole.ROLE_ADMIN);

        if (!roleOptional.isPresent()) {
            log.error("El rol no existe en base de datos");
            throw new RoleNotFoundException("El rol no existe en base de datos");
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

    public VeterinaryResponseDto findById(Long id) throws VeterinaryNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Error id no puede ser null");
        }

        Optional<Veterinary> veterinaryOptional = veterinaryRepository.findById(id);

        if (!veterinaryOptional.isPresent()) {
            throw new VeterinaryNotFoundException("Error Veterinary no encontrado");
        }

        Veterinary veterinaryFound = veterinaryOptional.get();

        VeterinaryResponseDto veterinaryResponseDto = new VeterinaryResponseDto();

        veterinaryResponseDto.setId(veterinaryFound.getId());
        veterinaryResponseDto.setUsername(veterinaryFound.getUsername());
        veterinaryResponseDto.setFirstName(veterinaryFound.getFirstName());
        veterinaryResponseDto.setLastName(veterinaryFound.getLastName());
        veterinaryResponseDto.setSalary(veterinaryFound.getSalary());

        // set visits
        List<VisitResponseDto> visitResponseDtoList = new ArrayList<>();
        for (Visit visit : veterinaryFound.getVisits()) {
            VisitResponseDto visitResponseDto = new VisitResponseDto();
            visitResponseDto.setId(visit.getId());
            visitResponseDto.setDate(visit.getDate());

            if (visit.getPet() != null) {
                visitResponseDto.setPetId(visit.getPet().getId());
            }

            visitResponseDto.setDescription(visit.getDescription());
            visitResponseDto.setDuration(visit.getDuration());
            if (visit.getVeterinary() != null) {
                visitResponseDto.setVeterinaryId(visit.getVeterinary().getId());
            }

            visitResponseDto.setVisitStatus(visit.getVisitStatus());
            visitResponseDtoList.add(visitResponseDto);
        }
        veterinaryResponseDto.setVisits(visitResponseDtoList);

        return veterinaryResponseDto;
    }

    public List<VeterinaryResponseDto> findAll() {
        List<Veterinary> veterinaryList = veterinaryRepository.findAll();

        List<VeterinaryResponseDto> veterinaryResponseDtoList = new ArrayList<>();
        for(Veterinary veterinary :veterinaryList){
            VeterinaryResponseDto veterinaryResponseDto = new VeterinaryResponseDto();

            veterinaryResponseDto.setId(veterinary.getId());
            veterinaryResponseDto.setUsername(veterinary.getUsername());
            veterinaryResponseDto.setFirstName(veterinary.getFirstName());
            veterinaryResponseDto.setLastName(veterinary.getLastName());
            veterinaryResponseDto.setSalary(veterinary.getSalary());

            // set visits
            List<VisitResponseDto> visitResponseDtoList = new ArrayList<>();
            for (Visit visit : veterinary.getVisits()) {
                VisitResponseDto visitResponseDto = new VisitResponseDto();
                visitResponseDto.setId(visit.getId());
                visitResponseDto.setDate(visit.getDate());

                if (visit.getPet() != null) {
                    visitResponseDto.setPetId(visit.getPet().getId());
                }

                visitResponseDto.setDescription(visit.getDescription());
                visitResponseDto.setDuration(visit.getDuration());
                if (visit.getVeterinary() != null) {
                    visitResponseDto.setVeterinaryId(visit.getVeterinary().getId());
                }

                visitResponseDto.setVisitStatus(visit.getVisitStatus());
                visitResponseDtoList.add(visitResponseDto);
            }
            veterinaryResponseDto.setVisits(visitResponseDtoList);

            veterinaryResponseDtoList.add(veterinaryResponseDto);
        }

        return veterinaryResponseDtoList;
    }
}
