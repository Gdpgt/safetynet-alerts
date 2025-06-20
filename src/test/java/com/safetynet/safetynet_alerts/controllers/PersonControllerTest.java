package com.safetynet.safetynet_alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynet_alerts.dto.PersonDTO;
import com.safetynet.safetynet_alerts.models.Person;
import com.safetynet.safetynet_alerts.services.PersonService;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonService personService;


    @Test
    void getAll_shouldReturnAllPersons() throws Exception {
        // Arrange
        List<Person> persons = List.of(
                TestUtils.createFakePerson("Alice"),
                TestUtils.createFakePerson("John")
        );

        when(personService.retrieveAll()).thenReturn(ResponseEntity.ok(persons));

        // Act & Assert
        mockMvc.perform(get("/person/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].firstName").value("John"));
    }


    @Test
    void addPerson_shouldReturnSuccessMessage_whenPersonIsNew() throws Exception {
        // Arrange
        PersonDTO dto = TestUtils.createFakePersonDTO();

        String requestBody = objectMapper.writeValueAsString(dto);

        when(personService.registerIfAbsent(any(PersonDTO.class)))
                .thenReturn(ResponseEntity.ok("Personne enregistrée."));

        // Act & Assert
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Personne enregistrée."));
    }


    @Test
    void updatePerson_shouldReturnSuccessMessage_whenPersonExists() throws Exception {
        // Arrange
        PersonDTO dto = TestUtils.createFakePersonDTO();
        String requestBody = objectMapper.writeValueAsString(dto);

        when(personService.updatePersonalInformation(any(PersonDTO.class))).thenReturn(ResponseEntity.ok("Informations personnelles mises à jour."));

        // Act & assert
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Informations personnelles mises à jour."));
    }


    @Test
    void deletePerson_shouldReturnSuccessMessage_whenPersonExists() throws Exception {
        // Arrange
        when(personService.delete(anyString(), anyString())).thenReturn(ResponseEntity.ok("Personne supprimée."));

        // Act & assert
        mockMvc.perform(delete("/person")
                        .param("firstName", "Alice")
                        .param("lastName", "Ebob"))
                .andExpect(status().isOk())
                .andExpect(content().string("Personne supprimée."));
    }

}
