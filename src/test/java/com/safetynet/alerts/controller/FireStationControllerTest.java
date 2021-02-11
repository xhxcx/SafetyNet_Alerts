package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

    private static FireStation fireStation = new FireStation();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationServiceMock;

    @BeforeEach
    private void setUpPerTest(){
        fireStation.setAddress("1 rue de paris");
        fireStation.setStation(99);
    }

    @Test
    public void getAllFireStations() throws Exception {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);
        when(fireStationServiceMock.getFireStations()).thenReturn(fireStationList);
        mockMvc.perform(get("/firestations")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].station", is(99)));
    }

    @Test
    public void addNewFireStations() throws Exception {
        when(fireStationServiceMock.saveFireStation(fireStation)).thenReturn(fireStation);
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(fireStation)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void modifyFireStations() throws Exception {
        when(fireStationServiceMock.updateFireStation(fireStation)).thenReturn(fireStation);
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(fireStation)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFireStations() throws Exception {
        when(fireStationServiceMock.deleteFireStation(fireStation)).thenReturn(true);
        mockMvc.perform(delete("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(fireStation)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
