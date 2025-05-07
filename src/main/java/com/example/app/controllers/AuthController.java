package com.example.app.controllers;

import com.example.app.models.Veterinary;
import com.example.app.models.dtos.AuthLoginDto;
import com.example.app.models.dtos.AuthResponseDto;
import com.example.app.services.JwtService;
import com.example.app.services.VeterinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private VeterinaryService veterinaryService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginDto user) {

        Optional<Veterinary> optionalUser = veterinaryService.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            Veterinary foundUser = optionalUser.get();
            if (veterinaryService.checkPassword(foundUser, user.getPassword())) {
                // Extract role names
                List<String> roleNames = foundUser.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList());

                String token = jwtService.generateToken(foundUser.getUsername(), roleNames.toString());

                AuthResponseDto response = new AuthResponseDto();
                response.setToken(token);
                response.setUsername(foundUser.getUsername());
                response.setRoles(roleNames);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}