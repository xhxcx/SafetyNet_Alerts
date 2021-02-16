package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CoveredPersonDTO;
import com.safetynet.alerts.model.dto.FireStationDistrictDTO;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.AlertsDateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation to manage and retrieve persons or information related to station fires operational area
 *
 */

@Service
public class FireStationDistrictServiceImpl implements FireStationDistrictService{

    private static final Logger log = LogManager.getLogger(FireStationDistrictService.class);

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;


    /**
     * Get the list of all phones of persons who live at an address covered by the given station number (may contains duplicates)
     *
     * @param stationNumber
     * @return a list of string containing phone numbers if at least one person is covered, otherwise return an empty list
     */
    @Override
    public List<String> getPhonesByStationNumber(int stationNumber) {
        List<String> phoneList = new ArrayList<>();
        List<Person> personsRelatedToFireStation = getPersonsByFireStation(stationNumber);

        if(!personsRelatedToFireStation.isEmpty()){
            phoneList = personsRelatedToFireStation.stream()
                    .map(Person::getPhone)
                    .collect(Collectors.toList());
            log.info("Phone alert :: all phones retrieved for station : " + stationNumber);
        }
        else
            log.error("Phone alert :: No person found for the district of fire station number : " + stationNumber);

        return phoneList;
    }

    /**
     * Get a list of CoveredPerson for a station number and count how many children and adults are covered.
     *
     * @param stationNumber
     * @return a FireStationDistrictDTO object containing the list of CoveredPerson and the counts
     */
    @Override
    public FireStationDistrictDTO getFireStationDistrictCoverage(int stationNumber) {
        FireStationDistrictDTO fireStationDistrictCoverage = new FireStationDistrictDTO();
        List<CoveredPersonDTO> coveredPersonList = new ArrayList<>();
        int adultCount = 0;
        int childrenCount = 0;

        List<Person> personList = getPersonsByFireStation(stationNumber);

        if(!personList.isEmpty()){
            //TODO comment faire plus propre ?
            for(Person p : personList){
                CoveredPersonDTO coveredPerson = new CoveredPersonDTO();
                int personAge = -1;
                //TODO prévoir méthode get medicalRecord par prénom/nom ou par personne
                MedicalRecord personRecord = medicalRecordRepository.getMedicalRecords()
                        .stream()
                        .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(p.getFirstName())&&medicalRecord.getLastName().equalsIgnoreCase(p.getLastName()))
                        .findAny()
                        .orElse(null);

                if (null != personRecord) {
                    personAge = new AlertsDateUtil().calculateAge(LocalDate.parse(personRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/uuuu")));
                }

                coveredPerson.setFirstName(p.getFirstName());
                coveredPerson.setLastName(p.getLastName());
                coveredPerson.setAddress(p.getAddress());
                coveredPerson.setPhone(p.getPhone());
                coveredPersonList.add(coveredPerson);

                if (personAge < 18)
                    childrenCount++;
                else
                    adultCount++;
            }
            fireStationDistrictCoverage.setCoveredPersonDTOList(coveredPersonList);
            fireStationDistrictCoverage.setAdultCount(adultCount);
            fireStationDistrictCoverage.setChildrenCount(childrenCount);
            log.info("Get district coverage :: List of persons and population counts are returned for station : " + stationNumber);
        }
        else
            log.error("Get district coverage :: No person covered by station : " + stationNumber);
        return fireStationDistrictCoverage;
    }


    /**
     * Get all persons covered by stations with a given station number
     * @param stationNumber
     * @return a list of persons or an empty list if no persons covered
     */
    private List<Person> getPersonsByFireStation(int stationNumber){
        List<Person> personList = new ArrayList<>();

        //Get all address covered by a station number
        List<String> addressList = fireStationRepository.getFireStations().stream()
                .filter(fireStation -> fireStation.getStation() == stationNumber)
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        //Build a list of person parsing every fire station addresses
        if (!addressList.isEmpty()) {
            //TODO sortir dans une méthode réutilisable pour les prochains endpoints (dans PersonRepository ?)
            List<Person>personAtAddress = new ArrayList<>();
            for (String address : addressList){
                personAtAddress = personRepository.getPersons().stream()
                        .filter(person -> person.getAddress().equalsIgnoreCase(address))
                        .collect(Collectors.toList());
                if(!personAtAddress.isEmpty()) {
                    personList.addAll(personAtAddress);
                }
            }
        }
        return personList;
    }
}