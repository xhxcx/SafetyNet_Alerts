package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordServiceMock;

    private static MedicalRecord medicalRecord = new MedicalRecord();

    @BeforeEach
    private void setUpPerTest(){
        ArrayList<String> medications = new ArrayList<>();
        medications.add("med1");
        ArrayList<String> allergies = new ArrayList<>();
        allergies.add("al");
        medicalRecord.setFirstName("toto");
        medicalRecord.setLastName("test");
        medicalRecord.setBirthdate("01/01/2021");
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);
    }

    @Test
    public void getAllMedicalRecordsTest() throws Exception {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);
        when(medicalRecordServiceMock.getMedicalRecords()).thenReturn(medicalRecordList);
        mockMvc.perform(get("/medicalRecords")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("toto")));
    }

    @Test
    public void createNewMedicalRecordTest() throws Exception {
        when(medicalRecordServiceMock.saveMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(medicalRecord)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void updateMedicalRecord() throws Exception {
        when(medicalRecordServiceMock.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(medicalRecord)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMedicalRecord() throws Exception {
        when(medicalRecordServiceMock.deleteMedicalRecord("toto","test")).thenReturn(true);
        mockMvc.perform(delete("/medicalRecord")
                .param("firstName",medicalRecord.getFirstName())
                .param("lastName",medicalRecord.getLastName()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
