package com.safetynet.safetynet_alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynet_alerts.dto.FirestationDTO;
import com.safetynet.safetynet_alerts.dto.UpdateFirestationDTO;
import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.services.FirestationService;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FirestationService firestationService;


    @Test
    void getAll_shouldReturnAllFirestations() throws Exception {
        // Arrange
        List<Firestation> firestations = List.of(TestUtils.createFakeFirestation("", 1), TestUtils.createFakeFirestation("", 2));

        when(firestationService.retrieveAll()).thenReturn(ResponseEntity.ok(firestations));

        // Act & assert
        mockMvc.perform(get("/firestation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].station").value(1))
                .andExpect(jsonPath("$[1].station").value(2));
    }


    @Test
    void addFirestation_shouldReturnSuccessMessage_whenFirestationIsNew() throws Exception {
        // Arrange
        FirestationDTO dto = TestUtils.createFakeFirestationDTO();

        String requestBody = objectMapper.writeValueAsString(dto);

        when(firestationService.registerIfAbsent(any(FirestationDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("La caserne de pompier a été ajoutée avec succès."));

        // Act & Assert
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string("La caserne de pompier a été ajoutée avec succès."));
    }


    @Test
    void updateFirestation_shouldReturnSuccessMessage_whenFirestationExists() throws Exception {
        // Arrange
        UpdateFirestationDTO dto = TestUtils.createFakeUpdateFirestationDTO();

        String requestBody = objectMapper.writeValueAsString(dto);

        when(firestationService.updateStationNumber(any(UpdateFirestationDTO.class)))
                .thenReturn(ResponseEntity.ok("La caserne de pompier a été mise à jour."));

        // Act & Assert
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("La caserne de pompier a été mise à jour."));
    }


    @Test
    void deleteFirestation_shouldReturnSuccessMessage_whenFirestationExists() throws Exception {
        // Arrange
        FirestationDTO dto = TestUtils.createFakeFirestationDTO();

        String requestBody = objectMapper.writeValueAsString(dto);

        when(firestationService.delete(any(FirestationDTO.class)))
                .thenReturn(ResponseEntity.ok("La caserne de pompier a été supprimée avec succès."));

        // Act & Assert
        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("La caserne de pompier a été supprimée avec succès."));
    }
    
}
