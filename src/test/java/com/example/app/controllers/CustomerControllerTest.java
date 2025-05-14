package com.example.app.controllers;

import com.example.app.exceptions.RoleNotFoundException;
import com.example.app.exceptions.UserExistsInDatabaseException;
import com.example.app.models.Customer;
import com.example.app.models.dtos.AuthRegisterCustomerDto;
import com.example.app.services.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = SecurityConfig.class)
//@SpringBootTest()
//@WebAppConfiguration
// TODO: ESTE TEST NO FUNCIONA
@WebMvcTest(CustomerControllerTest.class)
public class CustomerControllerTest {

    // Crea un contexto de la aplicación para poder inicializar MockMVC, esto genera un entorno de pruebas
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private CustomerService customerService;

    private MockMvc mockMvc; // esta clase nos servirá para simular peticiones HTTP

    private final ObjectMapper objectMapper = new ObjectMapper(); // es una herramienta para convertir objetos a JSON.

    @BeforeEach
    public void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser("spring")
    @Test
    @DisplayName("crear customer")
    public void addCustomerTest() throws
            Exception {

        Customer customer = Customer.builder()
                .username("mockitotest_username1")
                .password("1234")
                .firstName("mockitotest_firstname")
                .lastName("mockitotest_lastname")
                .address("mockitotest_address")
                .city("mockitotest_city")
                .telephone("mockitotest_telephone")
                .build();

        when(customerService.saveUser(any())).thenReturn(customer);

        AuthRegisterCustomerDto authRegisterCustomerDto = new AuthRegisterCustomerDto(
                "mockitotest_username1",
                "1234",
                "mockitotest_firstname",
                "mockitotest_lastname",
                "mockitotest_address",
                "mockitotest_city",
                "mockitotest_telephone"
        );

        String authRegisterCustomerDtoJson = objectMapper.writeValueAsString(authRegisterCustomerDto);

        MvcResult result = mockMvc.perform(post("/api/customer/add-customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRegisterCustomerDtoJson) // esto es el body
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("mockitotest_username1")))
                .andReturn();
    }
}
