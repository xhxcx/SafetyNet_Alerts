package com.safetynet.alerts.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputDataReader {

    private static final Logger log = LogManager.getLogger();

    private ObjectMapper objectMapper = new ObjectMapper();



    public List<Person> getAllPersonsFromFile(String filePath){
        System.out.println("/////////////////////////////////////  PERSONS READER ///////////////////////////////////");
        log.debug("Read all persons from data.json");
        List<Person> persons = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(new File(filePath));

            //Get persons
            JsonNode personNode = root.path("persons");

            if (personNode.isArray()){
                for (JsonNode node : personNode){
                    Person tmpPerson = new Person();
                    tmpPerson.setFirstName(node.path("firstName").textValue());
                    tmpPerson.setLastName(node.path("lastName").textValue());
                    tmpPerson.setAddress(node.path("address").textValue());
                    tmpPerson.setCity(node.path("city").textValue());
                    tmpPerson.setZip(node.path("zip").textValue());
                    tmpPerson.setPhone(node.path("phone").textValue());
                    tmpPerson.setEmail(node.path("email").textValue());
                    System.out.println(tmpPerson);

                    persons.add(tmpPerson);
                }
            }

        } catch (IOException e) {
            log.error("Error parsing input file to build persons list", e);
        }

        return persons;
    }

    public List<FireStation> getAllStationsFromFile(String filePath) {
        System.out.println("/////////////////////////////////////  FIRE STATIONS READER ///////////////////////////////////");
        log.debug("Read all fire stations from data.json");
        List<FireStation> fireStations = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(new File(filePath));

            //Get fire stations
            JsonNode fsNode = root.path("firestations");

            if (fsNode.isArray()){
                for (JsonNode node : fsNode){
                    FireStation station = new FireStation();
                    station.setAddress(node.path("address").textValue());
                    station.setStationNumber(Integer.parseInt(node.path("station").textValue()));
                    System.out.println(station);

                    fireStations.add(station);
                }
            }

        } catch (IOException e) {
            log.error("Error parsing input file to build fire stations list", e);
        }
        return fireStations;

    }

    public List<MedicalRecord> getAllMedicalRecordsFromFile(String filePath) {
        System.out.println("/////////////////////////////////////  MEDICAL RECORDS READER ///////////////////////////////////");
        log.debug("Read all medical records from data.json");
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(new File(filePath));

            //Get fire stations
            JsonNode recordNode = root.path("medicalrecords");

            if (recordNode.isArray()){
                for (JsonNode node : recordNode){
                    MedicalRecord record = new MedicalRecord();
                    record.setFirstName(node.path("firstName").textValue());
                    record.setLastName(node.path("lastName").textValue());
                    record.setBirthDate(node.path("birthdate").textValue());
                    List<String> medications = new ArrayList<>();
                    for(int i=0;i<node.path("medications").size();i++){
                        medications.add(node.path("medications").get(i).textValue());
                    }
                    record.setMedications(medications);
                    List<String> allergies = new ArrayList<>();
                    for(int i=0;i<node.path("allergies").size();i++){
                        allergies.add(node.path("allergies").get(i).textValue());
                    }
                    record.setAllergies(allergies);

                    System.out.println(record);

                    medicalRecords.add(record);
                }
            }

        } catch (IOException e) {
            log.error("Error parsing input file to build medical records list", e);
        }
        return medicalRecords;
    }
}
