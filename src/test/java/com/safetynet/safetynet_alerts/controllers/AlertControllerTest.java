package com.safetynet.safetynet_alerts.controllers;

import com.safetynet.safetynet_alerts.dto.*;
import com.safetynet.safetynet_alerts.services.AlertService;
import com.safetynet.safetynet_alerts.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlertService alertService;


    @Test
    void getPersonsCoveredByFirestationNumber_shouldReturnCoverageInformation() throws Exception {
        // Arrange
        FirestationCoverageDTO dto = TestUtils.createFakeFirestationCoverageDTO();
        when(alertService.retrievePersonsCoveredByFirestationNumber(anyInt())).thenReturn(ResponseEntity.ok(dto));

        // Act & Assert
        mockMvc.perform(get("/firestation").param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coveredPersons[0].firstName").value("John"))
                .andExpect(jsonPath("$.numberOfAdults").value(1));
    }


    @Test
    void getChildrenAndFamilyMembersByAddress_shouldReturnChildrenAndFamilyMembers() throws Exception {
        // Arrange
        ChildrenAndFamilyMembersByAddressDTO dto = TestUtils.createFakeChildrenAndFamilyMembersByAddressDTO();
        when(alertService.retrieveChildrenAndFamilyMembersByAddress(anyString())).thenReturn(ResponseEntity.ok(dto));

        // Act & Assert
        mockMvc.perform(get("/childAlert").param("address", "1 rue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children[0].firstName").value("Enfant"))
                .andExpect(jsonPath("$.familyMembers[0].firstName").value("Parent"));
    }


    @Test
    void getPhoneNumbersByFirestationNumber_shouldReturnPhoneNumbers() throws Exception {
        // Arrange
        PhoneNumbersByFirestationNumberDTO dto = TestUtils.createFakePhoneNumbersByFirestationNumberDTO();
        when(alertService.retrievePhoneNumbersByFirestationNumber(anyInt())).thenReturn(ResponseEntity.ok(dto));

        // Act & Assert
        mockMvc.perform(get("/phoneAlert").param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbersByFirestationNumber[0]").value("0600000000"));
    }


    @Test
    void getResidentsAndStationByAddress_shouldReturnResidentsAndStation() throws Exception {
        // Arrange
        ResidentsAndStationByAddressDTO dto = TestUtils.createFakeResidentsAndStationByAddressDTO();
        when(alertService.retrieveResidentsAndStationByAddress(anyString())).thenReturn(ResponseEntity.ok(dto));

        // Act & Assert
        mockMvc.perform(get("/fire").param("address", "10 Downing Street"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents[0].lastName").value("Doe"));
    }


    @Test
    void getResidentsByFirestationNumbers_shouldReturnResidentsGroupedByAddress() throws Exception {
        // Arrange
        ResidentsByFirestationNumbersDTO dto = TestUtils.createFakeResidentsByFirestationNumbersDTO();
        when(alertService.retrieveResidentsByFirestationNumbers(any())).thenReturn(ResponseEntity.ok(dto));

        // Act & Assert
        mockMvc.perform(get("/flood/stations").param("stations", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressesAndTheirResidents[0].address").value("10 Downing Street"));
    }


    @Test
    void getPersonsInfoByLastName_shouldReturnResidentsInfo() throws Exception {
        // Arrange
        List<ResidentByLastNameDTO> residents = List.of(TestUtils.createFakeResidentByLastNameDTO());
        when(alertService.retrievePersonsInfoByLastName(anyString())).thenReturn(ResponseEntity.ok(residents));

        // Act & Assert
        mockMvc.perform(get("/personInfo").param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }


    @Test
    void getEmailsByCity_shouldReturnEmails() throws Exception {
        // Arrange
        Set<String> emails = Set.of("john@doe.com");
        when(alertService.retrieveEmailsByCity(anyString())).thenReturn(ResponseEntity.ok(emails));

        // Act & Assert
        mockMvc.perform(get("/communityEmail").param("city", "Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("john@doe.com"));
    }

}
