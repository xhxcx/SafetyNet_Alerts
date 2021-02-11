package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.FireStationServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FireStationServiceIT {

    @Autowired
    private FireStationServiceImpl fireStationServiceUT;

    @Value("${outputFilePath}")
    private String outputFilePath;

    @BeforeEach
    private void setUpPerTest(){
        new File(outputFilePath).delete();
    }

/*    @AfterAll
    private static void cleanUp(){  new File(outputFilePath).delete();  }*/

    @Test
    public void fireStationService_shouldReturnAFireStationList_whenGetFireStations(){
        List<FireStation> fireStationList = fireStationServiceUT.getFireStations();
        assertThat(fireStationList.size()).isEqualTo(13);
    }

    @Test
    public void fireStationService_shouldReturnTheCreatedFireStation_andAddTheNewFireStationInOutputFile_whenSaveFireStation(){
        FireStation fireStationToCreate = new FireStation();
        fireStationToCreate.setAddress("1 rue de paris");
        fireStationToCreate.setStation(99);
        FireStation resultFireStation = fireStationServiceUT.saveFireStation(fireStationToCreate);

        assertThat(resultFireStation.getStation()).isEqualTo(99);
        assertThat(resultFireStation.getAddress()).isEqualTo("1 rue de paris");
        assertThat(fireStationServiceUT.getFireStations().contains(fireStationToCreate));
        assertThat(new File(outputFilePath)).exists();
    }

    @Test
    public void fireStationService_shouldReturnTheUpdatedFireStation_andModifyFireStationInOutputFile_whenUpdateFireStation(){
        FireStation fireStationToModify = new FireStation();
        fireStationToModify.setAddress("951 LoneTree Rd");
        fireStationToModify.setStation(2);

        FireStation resultFireStation = fireStationServiceUT.updateFireStation(fireStationToModify);

        assertThat(resultFireStation.getAddress()).isEqualTo("951 LoneTree Rd");
        assertThat(resultFireStation.getStation()).isEqualTo(2);
        assertThat(new File(outputFilePath)).exists();
    }

    @Test
    public void fireStationService_shouldReturnTrue_andDeleteFireStationInOutputFile_whenDeleteFireStation(){
        FireStation fireStationToDelete = new FireStation();
        fireStationToDelete.setAddress("951 LoneTree Rd");
        fireStationToDelete.setStation(2);

        boolean result = fireStationServiceUT.deleteFireStation(fireStationToDelete);

        assertThat(result).isTrue();
        assertThat(new File(outputFilePath)).exists();
    }
}
