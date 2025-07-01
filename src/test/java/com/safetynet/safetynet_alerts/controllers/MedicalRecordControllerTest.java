package com.safetynet.safetynet_alerts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynet_alerts.dto.MedicalRecordDTO;
import com.safetynet.safetynet_alerts.models.MedicalRecord;
import com.safetynet.safetynet_alerts.services.MedicalRecordService;
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

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MedicalRecordService medicalRecordService;


    @Test
    void getAll_shouldReturnAllMedicalRecords() throws Exception {
        // Arrange
        List<MedicalRecord> medicalRecords = List.of(
                TestUtils.createFakeMedicalRecord("Alice"),
                TestUtils.createFakeMedicalRecord("John")
        );

        when(medicalRecordService.retrieveAll()).thenReturn(ResponseEntity.ok(medicalRecords));

        // Act & Assert
        mockMvc.perform(get("/medicalRecord/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].firstName").value("John"));
    }


    @Test
    void addMedicalRecord_shouldReturnSuccessMessage_whenMedicalRecordIsNew() throws Exception {
        // Arrange
        MedicalRecordDTO dto = TestUtils.createFakeMedicalRecordDTO();

        String requestBody = objectMapper.writeValueAsString(dto);

        when(medicalRecordService.registerIfAbsent(any(MedicalRecordDTO.class)))
                .thenReturn(ResponseEntity.ok("Dossier médical ajouté."));

        // Act & Assert
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical ajouté."));
    }


    @Test
    void updateMedicalRecord_shouldReturnSuccessMessage_whenMedicalRecordExists() throws Exception {
        // Arrange
        MedicalRecordDTO dto = TestUtils.createFakeMedicalRecordDTO();

        String requestBody = objectMapper.writeValueAsString(dto);

        when(medicalRecordService.update(any(MedicalRecordDTO.class)))
                .thenReturn(ResponseEntity.ok("Dossier médical mis à jour."));

        // Act & Assert
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical mis à jour."));
    }


    @Test
    void deleteMedicalRecord_shouldReturnSuccessMessage_whenMedicalRecordExists() throws Exception {
        // Arrange
        when(medicalRecordService.delete(anyString(), anyString()))
                .thenReturn(ResponseEntity.ok("Dossier médical supprimé."));

        // Act & Assert
        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Alice")
                        .param("lastName", "Ebob"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical supprimé."));
    }

}
