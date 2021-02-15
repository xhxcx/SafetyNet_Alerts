package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FireStationDistrictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationDistrictController.class)
public class FireStationDistrictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationDistrictService fireStationDistrictServiceMock;


    @Test
    public void getPhonesForFireStationTest() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                .param("firestation","1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
