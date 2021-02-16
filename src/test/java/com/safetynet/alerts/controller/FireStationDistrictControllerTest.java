package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.dto.CoveredPersonDTO;
import com.safetynet.alerts.model.dto.FireStationDistrictDTO;
import com.safetynet.alerts.service.FireStationDistrictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationDistrictController.class)
public class FireStationDistrictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationDistrictService fireStationDistrictServiceMock;


    @Test
    public void getPhonesForFireStationTest() throws Exception {
        List<String> phoneList = new ArrayList<>();
        phoneList.add("0199999999");

        when(fireStationDistrictServiceMock.getPhonesByStationNumber(any(Integer.class))).thenReturn(phoneList);

        mockMvc.perform(get("/phoneAlert")
                .param("firestation","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("0199999999")));

    }

    @Test
    public void getPersonsCoveredByFireStationTest() throws Exception {
        FireStationDistrictDTO fireStationDistrictDTO = new FireStationDistrictDTO();
        CoveredPersonDTO coveredPersonDTO = new CoveredPersonDTO();

        coveredPersonDTO.setFirstName("Tyler");

        fireStationDistrictDTO.setCoveredPersonDTOList(new ArrayList<CoveredPersonDTO>(Arrays.asList(coveredPersonDTO)));
        fireStationDistrictDTO.setAdultCount(1);

        when(fireStationDistrictServiceMock.getFireStationDistrictCoverage(any(Integer.class))).thenReturn(fireStationDistrictDTO);
        mockMvc.perform(get("/firestation")
                .param("stationNumber","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coveredPersonDTOList[0].firstName", is("Tyler")))
                .andExpect(jsonPath("$.adultCount", is(1)));
    }
}
