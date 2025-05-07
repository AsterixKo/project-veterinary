package com.example.app.services;

import com.example.app.models.ERole;
import com.example.app.models.Role;
import com.example.app.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public boolean generateRolesIfNotExists() {

        boolean isGenerated = false;
        Optional<Role> roleUserOptional = roleRepository.findByName(ERole.ROLE_USER);
        if(!roleUserOptional.isPresent()){
            roleRepository.save(Role.builder().name(ERole.ROLE_USER).build());
            isGenerated = true;
        }

        Optional<Role> roleModeratorOptional = roleRepository.findByName(ERole.ROLE_MODERATOR);
        if(!roleModeratorOptional.isPresent()){
            roleRepository.save(Role.builder().name(ERole.ROLE_MODERATOR).build());
            isGenerated = true;
        }

        Optional<Role> roleAdminOptional = roleRepository.findByName(ERole.ROLE_ADMIN);
        if(!roleAdminOptional.isPresent()){
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).build());
            isGenerated = true;
        }

        return isGenerated;

    }
}
