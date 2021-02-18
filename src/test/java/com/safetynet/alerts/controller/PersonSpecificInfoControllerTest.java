package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonSpecificInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonSpecificInfoController.class)
public class PersonSpecificInfoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonSpecificInfoService personSpecificInfoServiceMock;

    @Test
    public void getPersonInfoByNameTest() throws Exception {
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setLastName("durden");

        when(personSpecificInfoServiceMock.getPersonInfo(any(String.class),any(String.class))).thenReturn(new ArrayList<>(Arrays.asList(personInfoDTO)));

        mockMvc.perform(get("/personInfo")
                .param("firstName","tyler")
                .param("lastName","durden"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName",is("durden")));
    }
}
