package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationDistrictServiceImpl implements FireStationDistrictService{

    private static final Logger log = LogManager.getLogger(FireStationDistrictService.class);

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<String> getPhonesByStationNumber(int stationNumber) {
        List<String> phoneList = new ArrayList<>();
        List<Person> personsRelatedToFireStation = getPersonsByFireStation(stationNumber);

        if(!personsRelatedToFireStation.isEmpty()){
            for(Person p : personsRelatedToFireStation){
                phoneList.add(p.getPhone());
            }
            log.info("Phone alert :: all phones retrieved for station : " + stationNumber);
        }
        else
            log.error("Phone alert :: No person found for the district of fire station number : " + stationNumber);

        return phoneList;
    }

    private List<Person> getPersonsByFireStation(int stationNumber){
        List<Person> personList = new ArrayList<>();
        List<String> addressList = fireStationRepository.getFireStations().stream()
                .filter(fireStation -> fireStation.getStation() == stationNumber)
                .map(fireStation -> fireStation.getAddress())
                .collect(Collectors.toList());

        if (!addressList.isEmpty()) {
            //TODO sortir dans une méthode réutilisable pour les prochains endpoints (dans PersonRepository ?)
            List<Person>personAtAddress = new ArrayList<>();
            for (String address : addressList){
                personAtAddress = personRepository.getPersons().stream()
                        .filter(person -> person.getAddress().equalsIgnoreCase(address))
                        .collect(Collectors.toList());
                if(!personAtAddress.isEmpty()) {
                    for (Person p : personAtAddress) {
                        personList.add(p);
                    }
                }
            }
        }
        return personList;
    }
}