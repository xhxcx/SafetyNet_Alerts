package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.*;
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
import java.util.*;
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
            for(Person p : personList){
                CoveredPersonDTO coveredPerson = new CoveredPersonDTO();
                int personAge = -1;
                MedicalRecord pRecord = medicalRecordRepository.getMedicalRecordByName(p.getFirstName(),p.getLastName());

                if (null != pRecord) {
                    personAge = new AlertsDateUtil().calculateAge(LocalDate.parse(pRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/uuuu")));
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
     * Get the list of potential victims living in the area of the given address and the fire station numbers associated to the address
     * Victim information returned : last name, phone, age, medications and allergies
     *
     * @param address
     * @return FireDTO object containing the list of victims and the list of fire station numbers
     */
    @Override
    public FireDTO getFireInformationByAddress(String address) {
        FireDTO fireDTO = new FireDTO();
        List<DisasterVictimDTO> victimList = new ArrayList<>();

        if(!address.isEmpty() && address != null) {
            //Get station number par address + FireDTO set stationNumbers
            List<Integer> stationNumbersForAddress = fireStationRepository.getFireStations()
                    .stream()
                    .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                    .map(FireStation::getStation)
                    .collect(Collectors.toList());
            if (!stationNumbersForAddress.isEmpty()) {
                fireDTO.setStationNumberList(stationNumbersForAddress);
            }
            //Get person by address and process the list to match victims model
            List<Person> personList = personRepository.getPersonsByAddress(address);

            personList.forEach(person -> victimList.add(processPersonsIntoVictims(person)));
            fireDTO.setVictimList(victimList);
        }
        else
            log.error("Get fire information :: Failed, can't retrieve information for null or empty address");
        return fireDTO;
    }

    /**
     * Get the list of potential victims covered by fire stations of given station numbers
     * Victim information returned : last name, phone, age, medications and allergies
     *
     * @param stationNumbers
     * @return FloodDTO object containing the list of victims grouped by address of empty map if no persons covered by stations
     */
    @Override
    public FloodDTO getFloodInformationByStations(List<Integer> stationNumbers) {
        FloodDTO floodDTO = new FloodDTO();
        //Get person list from station numbers
        List<Person> globalPersonList = new ArrayList<>();
        if (!stationNumbers.isEmpty() && stationNumbers != null) {
            stationNumbers.forEach(station -> globalPersonList.addAll(getPersonsByFireStation(station)));

            if (!globalPersonList.isEmpty()) {
                //Create a map to group persons by address and then transform persons into DisasterVictimDTO
                Map<String, List<Person>> personsGroupedByAddress = globalPersonList.stream().collect(Collectors.groupingBy(Person::getAddress));
                Map<String, List<DisasterVictimDTO>> familyMap = personsGroupedByAddress.entrySet().stream().collect(Collectors
                        .toMap(Map.Entry::getKey, entry -> entry.getValue()
                                .stream().map(person -> processPersonsIntoVictims(person))
                                .collect(Collectors.toList())));


                floodDTO.setFamilyByAddressList(familyMap);
            } else {
                floodDTO.setFamilyByAddressList(new HashMap<>());
                log.info("Flood information :: Nothing to retrieve for station numbers : " + stationNumbers);
            }
        }
        else{
            log.error("Flood information :: Failed, can't retrieve information for empty or null stations");
            return null;
        }

        return floodDTO;
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
                .distinct()
                .collect(Collectors.toList());

        //Build a list of person parsing every fire station addresses
        if (!addressList.isEmpty()) {
            addressList.forEach(address -> {
                List<Person> personsAtAddress = personRepository.getPersonsByAddress(address);
                if (!personsAtAddress.isEmpty())
                    personList.addAll(personsAtAddress);
            });
        }
        return personList;
    }

    /**
     * Aggregate information from person and medical record to build a DisasterVictimDTO object
     * A DisasterVictimDTO contains following information : lastName, phone, age, medications, allergies
     *
     * @param person
     * @return a DisasterVictimDTO or null if parameter is null
     */
    private DisasterVictimDTO processPersonsIntoVictims(Person person) {
        DisasterVictimDTO victim = new DisasterVictimDTO();

        if (person != null) {
            MedicalRecord victimRecord = medicalRecordRepository.getMedicalRecordByName(person.getFirstName(), person.getLastName());

            victim.setLastName(person.getLastName());
            victim.setPhone(person.getPhone());

            if (victimRecord != null) {
                victim.setAge(new AlertsDateUtil().calculateAge(LocalDate.parse(victimRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/uuuu"))));
                victim.setMedications(victimRecord.getMedications());
                victim.setAllergies(victimRecord.getAllergies());
            }
        }
        else
            return null;
        return victim;
    }
}