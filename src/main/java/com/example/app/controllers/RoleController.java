package com.example.app.controllers;

import com.example.app.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/generate-roles")
    @ResponseStatus(HttpStatus.OK)
    public String generateRoles() {

        boolean isGenerated = roleService.generateRolesIfNotExists();

        if(isGenerated){
            return "Roles generated";
        } else {
            return "Roles exists in database";
        }
    }
}
