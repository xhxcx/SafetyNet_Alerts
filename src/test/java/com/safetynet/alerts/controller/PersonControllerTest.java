package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    private String existingPersonJson = "{ \"firstName\":\"Eric\", \"lastName\":\"Cadigan\", \"address\":\"951 LoneTree Rd\", \"city\":\"Culver\", \"zip\":\"97451\", \"phone\":\"841-874-7458\", \"email\":\"gramps@email.com\" }";
    private static Person person = new Person();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personServiceMock;

    @BeforeEach
    private void setUpPerTest(){
        person.setFirstName("toto");
        person.setLastName("test");
        person.setAddress("25 rue de paris");
        person.setCity("Paris");
        person.setZip("75000");
        person.setPhone("0120304050");
        person.setEmail("toto@test.com");
    }

    @Nested
    @DisplayName("Controller CRUD endpoints")
    class CRUDEndpointsTests{
        @Test
        public void getAllPersonsTest() throws Exception {
            List<Person> personList = new ArrayList<>();
            personList.add(person);
            when(personServiceMock.getAllPersons()).thenReturn(personList);
            mockMvc.perform(get("/persons")).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName", is("toto")));
        }

        @Test
        public void createNewPersonTest() throws Exception {
            when(personServiceMock.savePerson(person)).thenReturn(person);
            mockMvc.perform(post("/person")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(person)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        public void updatePerson() throws Exception {
            when(personServiceMock.modifyPerson(person)).thenReturn(person);
            mockMvc.perform(put("/person")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(existingPersonJson))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        public void deletePerson() throws Exception {
            mockMvc.perform(delete("/person")
                    .param("firstName",person.getFirstName())
                    .param("lastName",person.getLastName()))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void getPersonsEmailsByCity() throws Exception {
        mockMvc.perform(get("/communityEmail")
                .param("city","Paris"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
