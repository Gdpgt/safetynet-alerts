package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.models.Firestation;
import com.safetynet.safetynet_alerts.services.FirestationService;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FirestationService firestationService;


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




}
