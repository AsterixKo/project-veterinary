package com.example.app.services;

import com.example.app.exceptions.PetNotFoundException;
import com.example.app.exceptions.VeterinaryNotFoundException;
import com.example.app.models.Pet;
import com.example.app.models.Veterinary;
import com.example.app.models.Visit;
import com.example.app.models.VisitStatus;
import com.example.app.repositories.PetRepository;
import com.example.app.repositories.VeterinaryRepository;
import com.example.app.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class VisitService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinaryRepository veterinaryRepository;

    @Autowired
    private VisitRepository visitRepository;

    // visitService.saveVisit(idVeterinary, visitCreationDto.getIdPet(), visit);
    public Visit saveNewVisit(Long idVeterinary, Long idPet, Visit visit) throws VeterinaryNotFoundException, PetNotFoundException {

        if(idVeterinary == null) {
            log.error("idVeterinary no puede ser null");
            throw new IllegalArgumentException("idVeterinary no puede ser null");
        } else if( idPet == null) {
            log.error("idPet no puede ser null");
            throw new IllegalArgumentException("idPet no puede ser null");
        } else if (visit == null) {
            log.error("visit no puede ser null");
            throw new IllegalArgumentException("visit no puede ser null");
        }

        if(visit.getDate() == null){
            log.error("date no puede ser null");
            throw new IllegalArgumentException("date no puede ser null");
        }

        if(visit.getDuration() <=0){
            log.error("duration no puede ser menor o igual a 0");
            throw new IllegalArgumentException("duration no puede ser menor o igual a 0");
        }

        if(visit.getId() != null){
            log.error("error no puedes crear un Visit que ya tiene id");
            throw  new IllegalArgumentException("error no puedes crear un Visit que ya tiene id");
        }

        Optional<Veterinary> veterinaryOptional = veterinaryRepository.findById(idVeterinary);

        if(!veterinaryOptional.isPresent()){
            log.error("Error el veterinary no se ha encontrado");
            throw new VeterinaryNotFoundException("Error el veterinary no se ha encontrado");
        }

        Optional<Pet> petOptional = petRepository.findById(idPet);

        if(!petOptional.isPresent()){
            log.error("Error el Pet no se ha encontrado");
            throw new PetNotFoundException("Error el Pet no se ha encontrado");
        }

        // rellenamos campos
        visit.setPet(petOptional.get());
        visit.setVeterinary(veterinaryOptional.get());
        visit.setVisitStatus(VisitStatus.ACTIVE);

        log.info("guardando visit: {}", visit);

        return visitRepository.save(visit);
    }
}
