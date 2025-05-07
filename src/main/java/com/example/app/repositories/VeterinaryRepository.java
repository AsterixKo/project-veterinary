package com.example.app.repositories;

import com.example.app.models.Veterinary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinaryRepository extends JpaRepository<Veterinary, Long> {
    Optional<Veterinary> findByUsername(String username);
}
