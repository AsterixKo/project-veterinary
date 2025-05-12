package com.example.app.services;

import com.example.app.exceptions.PetNotFoundException;
import com.example.app.exceptions.VeterinaryNotFoundException;
import com.example.app.exceptions.VisitNotFoundException;
import com.example.app.models.Pet;
import com.example.app.models.Veterinary;
import com.example.app.models.Visit;
import com.example.app.models.VisitStatus;
import com.example.app.models.dtos.VisitResponseDto;
import com.example.app.repositories.PetRepository;
import com.example.app.repositories.VeterinaryRepository;
import com.example.app.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    public VisitResponseDto saveNewVisit(Long idVeterinary, Long idPet, Visit visit)
            throws VeterinaryNotFoundException,
            PetNotFoundException {

        log.info("validaciones de IllegalArgumentException");
        if (idVeterinary == null) {
            log.error("idVeterinary no puede ser null");
            throw new IllegalArgumentException("idVeterinary no puede ser null");
        } else if (idPet == null) {
            log.error("idPet no puede ser null");
            throw new IllegalArgumentException("idPet no puede ser null");
        } else if (visit == null) {
            log.error("visit no puede ser null");
            throw new IllegalArgumentException("visit no puede ser null");
        }

        if (visit.getDate() == null) {
            log.error("date no puede ser null");
            throw new IllegalArgumentException("date no puede ser null");
        }

        if (visit.getDuration() <= 0) {
            log.error("duration no puede ser menor o igual a 0");
            throw new IllegalArgumentException("duration no puede ser menor o igual a 0");
        }

        if (visit.getId() != null) {
            log.error("error no puedes crear un Visit que ya tiene id");
            throw new IllegalArgumentException("error no puedes crear un Visit que ya tiene id");
        }

        Optional<Veterinary> veterinaryOptional = veterinaryRepository.findById(idVeterinary);

        if (!veterinaryOptional.isPresent()) {
            log.error("Error el veterinary no se ha encontrado");
            throw new VeterinaryNotFoundException("Error el veterinary no se ha encontrado");
        }

        Optional<Pet> petOptional = petRepository.findById(idPet);

        if (!petOptional.isPresent()) {
            log.error("Error el Pet no se ha encontrado");
            throw new PetNotFoundException("Error el Pet no se ha encontrado");
        }

        // rellenamos campos
        visit.setPet(petOptional.get());
        visit.setVeterinary(veterinaryOptional.get());
        visit.setVisitStatus(VisitStatus.ACTIVE);

        log.info("guardando visit");

        Visit visitSaved = visitRepository.save(visit);

        VisitResponseDto visitResponseDto = VisitResponseDto
                .builder()
                .id(visitSaved.getId())
                .date(visitSaved.getDate())
                .petId(visitSaved.getPet() != null ? visitSaved.getPet().getId() : null)
                .description(visitSaved.getDescription())
                .duration(visitSaved.getDuration())
                .veterinaryId(visitSaved.getVeterinary() != null ? visitSaved.getVeterinary().getId() : null)
                .visitStatus(visitSaved.getVisitStatus())
                .build();

        return visitResponseDto;
    }

    @Transactional
    public VisitResponseDto updateVisitStatus(Long id, String visitStatus) throws VisitNotFoundException {

        if (id == null) {
            throw new IllegalArgumentException("Id no puede ser null");
        }

        VisitStatus visitStatusEnum = VisitStatus.valueOf(visitStatus);

        Optional<Visit> visitFound = visitRepository.findById(id);

        if (!visitFound.isPresent()) {
            throw new VisitNotFoundException("Error visit no encontrado con ese id");
        }

        visitFound.get().setVisitStatus(visitStatusEnum);

        visitRepository.save(visitFound.get());

        VisitResponseDto visitResponseDto = VisitResponseDto
                .builder()
                .id(visitFound.get().getId())
                .date(visitFound.get().getDate())
                .petId(visitFound.get().getPet() != null ? visitFound.get().getPet().getId() : null)
                .description(visitFound.get().getDescription())
                .duration(visitFound.get().getDuration())
                .veterinaryId(visitFound.get().getVeterinary() != null ?
                        visitFound.get().getVeterinary().getId() : null)
                .visitStatus(visitFound.get().getVisitStatus())
                .build();

        return visitResponseDto;

    }
}
