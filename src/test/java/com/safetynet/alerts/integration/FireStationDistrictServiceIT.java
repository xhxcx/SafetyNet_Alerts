package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.dto.CoveredPersonDTO;
import com.safetynet.alerts.model.dto.DisasterVictimDTO;
import com.safetynet.alerts.model.dto.FloodDTO;
import com.safetynet.alerts.service.FireStationDistrictServiceImpl;
import com.safetynet.alerts.utils.AlertsDateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
public class FireStationDistrictServiceIT {

    @Autowired
    private FireStationDistrictServiceImpl fireStationDistrictServiceUT;


    @Test
    public void fireStationDistrictService_shouldReturnAListOfPhones_whenGetPhonesByStationNumber() {
        List<String> expectedPhones = new ArrayList<>();
        expectedPhones.add("841-874-6874");
        expectedPhones.add("841-874-9845");
        expectedPhones.add("841-874-8888");
        expectedPhones.add("841-874-9888");

        assertThat(fireStationDistrictServiceUT.getPhonesByStationNumber(4).size()).isEqualTo(4);
        assertThat(fireStationDistrictServiceUT.getPhonesByStationNumber(4).containsAll(expectedPhones)).isTrue();
    }

    @Test
    public void fireStationDistrictService_shouldReturnAListCoveredPerson_andACountRegardingPersonAge_whenGetFireStationDistrictCoverage() {
        CoveredPersonDTO expectedPerson = new CoveredPersonDTO();
        expectedPerson.setFirstName("Tony");
        expectedPerson.setLastName("Cooper");
        expectedPerson.setPhone("841-874-6874");
        expectedPerson.setAddress("112 Steppes Pl");

        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getCoveredPersonDTOList().size()).isEqualTo(4);
        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getChildrenCount()).isEqualTo(0);
        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getAdultCount()).isEqualTo(4);
        assertThat(fireStationDistrictServiceUT.getFireStationDistrictCoverage(4).getCoveredPersonDTOList().contains(expectedPerson)).isTrue();
    }

    @Test
    public void fireStationDistrictService_shouldReturnAListDisasterVictim_andAListOfStationNumber_whenGetFireInformationByAddress() {
        DisasterVictimDTO expectedVictim = new DisasterVictimDTO();
        expectedVictim.setLastName("Peters");
        expectedVictim.setPhone("841-874-8888");
        expectedVictim.setAge(new AlertsDateUtil().calculateAge(LocalDate.parse("04/06/1965", DateTimeFormatter.ofPattern("MM/dd/uuuu"))));
        expectedVictim.setMedications(new ArrayList<>());
        expectedVictim.setAllergies(new ArrayList<>());

        List<Integer> stationNumberList = new ArrayList<>();
        stationNumberList.add(3);
        stationNumberList.add(4);

        assertThat(fireStationDistrictServiceUT.getFireInformationByAddress("112 Steppes Pl").getVictimList().size()).isEqualTo(3);
        assertThat(fireStationDistrictServiceUT.getFireInformationByAddress("112 Steppes Pl").getVictimList().contains(expectedVictim)).isTrue();

        assertThat(fireStationDistrictServiceUT.getFireInformationByAddress("112 Steppes Pl").getStationNumberList().size()).isEqualTo(2);
        assertThat(fireStationDistrictServiceUT.getFireInformationByAddress("112 Steppes Pl").getStationNumberList().equals(stationNumberList)).isTrue();
    }

    @Test
    public void fireStationDistrictService_shouldReturnAMapWithAddressAsKeyAndListDisasterVictimAsValue_whenGetFloodInformationByStations() {
        DisasterVictimDTO expectedVictim = new DisasterVictimDTO();
        expectedVictim.setLastName("Peters");
        expectedVictim.setPhone("841-874-8888");
        expectedVictim.setAge(new AlertsDateUtil().calculateAge(LocalDate.parse("04/06/1965", DateTimeFormatter.ofPattern("MM/dd/uuuu"))));
        expectedVictim.setMedications(new ArrayList<>());
        expectedVictim.setAllergies(new ArrayList<>());

        List<Integer> stationNumberList = new ArrayList<>();
        stationNumberList.add(3);
        stationNumberList.add(4);

        FloodDTO floodDTO = fireStationDistrictServiceUT.getFloodInformationByStations(stationNumberList);

        assertThat(floodDTO.getFamilyByAddressList().size()).isEqualTo(5);
        assertThat(floodDTO.getFamilyByAddressList().containsKey("112 Steppes Pl")).isTrue();
        assertThat(floodDTO.getFamilyByAddressList().get("112 Steppes Pl").contains(expectedVictim)).isTrue();
        assertThat(floodDTO.getFamilyByAddressList().containsKey("834 Binoc Ave")).isTrue();
        assertThat(floodDTO.getFamilyByAddressList().get("748 Townings Dr").size()).isEqualTo(2);

    }
}
