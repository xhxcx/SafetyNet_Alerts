package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.dto.*;
import com.safetynet.alerts.service.FireStationDistrictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

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

    @Test
    public void getFireCoverageByAddressTest() throws Exception {
        FireDTO fireDTO = new FireDTO();
        DisasterVictimDTO disasterVictimDTO = new DisasterVictimDTO();

        disasterVictimDTO.setLastName("Durden");

        fireDTO.setVictimList(new ArrayList<DisasterVictimDTO>(Arrays.asList(disasterVictimDTO)));
        fireDTO.setStationNumberList(new ArrayList<Integer>(Arrays.asList(1)));

        when(fireStationDistrictServiceMock.getFireInformationByAddress(any(String.class))).thenReturn(fireDTO);
        mockMvc.perform(get("/fire")
                .param("address","1 rue de paris"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.victimList[0].lastName", is("Durden")))
                .andExpect(jsonPath("$.stationNumberList[0]", is(1)));
    }

    @Test
    public void getFloodCoverageByStationsTest() throws Exception {
        FloodDTO floodDTO = new FloodDTO();
        DisasterVictimDTO disasterVictimDTO = new DisasterVictimDTO();
        disasterVictimDTO.setLastName("Durden");

        FireStation fireStation = new FireStation();
        fireStation.setAddress("1 rue de paris");
        fireStation.setStation(1);

        Map<String, List<DisasterVictimDTO>> floodMap = new HashMap<>();
        floodMap.put("1 rue de paris",new ArrayList<>(Arrays.asList(disasterVictimDTO)));

        floodDTO.setFamilyByAddressList(floodMap);

        when(fireStationDistrictServiceMock.getFloodInformationByStations(any(List.class))).thenReturn(floodDTO);
        mockMvc.perform(get("/flood/stations")
                .param("stations","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.familyByAddressList.['1 rue de paris'][0].lastName", is("Durden")));
    }
}
