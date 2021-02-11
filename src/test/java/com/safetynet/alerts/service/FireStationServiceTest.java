package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FireStationServiceTest {

    private static FireStation fireStation;
    private static List<FireStation> fireStationList;

    @Mock
    private FireStationRepository fireStationRepositoryMock;

    @InjectMocks
    private FireStationServiceImpl fireStationService;

    @BeforeEach
    private void setUp(){
        fireStation = new FireStation();
        fireStation.setAddress("12 rue de paris");
        fireStation.setStation(1);

        fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);
    }
    @Nested
    @DisplayName("Get fire stations tests")
    class GetFireStationCasesTests{

        @Test
        public void getFireStations(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.getFireStations().size()).isEqualTo(1);
            assertThat(fireStationService.getFireStations().contains(fireStation)).isTrue();
            verify(fireStationRepositoryMock, Mockito.times(2)).getFireStations();
        }

        @Test
        public void getFireStationsReturnEmptyListTest(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(new ArrayList<>());
            assertThat(fireStationService.getFireStations()).isEmpty();
            verify(fireStationRepositoryMock, Mockito.times(1)).getFireStations();
        }

        @Test
        public void getFireStationsReturnNullTest(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(null);
            assertThat(fireStationService.getFireStations()).isNull();
            verify(fireStationRepositoryMock, Mockito.times(1)).getFireStations();
        }
    }

    @Nested
    @DisplayName("Save fire stations tests")
    class SaveFireStationCasesTests{
        @Test
        public void saveFireStationTest(){
            FireStation newFireStation = new FireStation();
            newFireStation.setAddress("25 rue de Nantes");
            newFireStation.setStation(12);

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(fireStationRepositoryMock.createFireStation(any(FireStation.class))).thenReturn(newFireStation);
            assertThat(fireStationService.saveFireStation(newFireStation)).isEqualTo(newFireStation);
            verify(fireStationRepositoryMock, Mockito.times(1)).createFireStation(any(FireStation.class));
        }

        @Test
        public void saveAlreadyExistingFireStationTest(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(fireStationRepositoryMock.createFireStation(fireStation)).thenReturn(null);
            assertThat(fireStationService.saveFireStation(fireStation)).isNull();
            verify(fireStationRepositoryMock, Mockito.times(0)).createFireStation(any(FireStation.class));
        }

        @Test
        public void saveANullFireStationTest(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.saveFireStation(null)).isNull();
            verify(fireStationRepositoryMock, Mockito.times(0)).createFireStation(any(FireStation.class));
        }

    }

    @Nested
    @DisplayName("Update fire stations tests")
    class UpdateFireStationCasesTests{
        @Test
        public void updateFireStationTest(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            when(fireStationRepositoryMock.updateFireStation(any(FireStation.class))).thenReturn(fireStation);
            assertThat(fireStationService.updateFireStation(fireStation)).isEqualTo(fireStation);
            verify(fireStationRepositoryMock,Mockito.times(1)).updateFireStation(any(FireStation.class));

        }

        @Test
        public void updateNonExistingFireStationTest(){
            FireStation newFireStation = new FireStation();
            newFireStation.setAddress("25 rue de Nantes");
            newFireStation.setStation(12);

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.updateFireStation(newFireStation)).isNull();
            verify(fireStationRepositoryMock,Mockito.times(0)).updateFireStation(any(FireStation.class));

        }

        @Test
        public void updateNullFireStationTest(){
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.updateFireStation(null)).isNull();
            verify(fireStationRepositoryMock,Mockito.times(0)).updateFireStation(any(FireStation.class));

        }
    }

    @Nested
    @DisplayName("Delete fire stations tests")
    class DeleteFireStationCasesTests{
        @Test
        public void deleteFireStationTest() {
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.deleteFireStation(fireStation)).isTrue();
            verify(fireStationRepositoryMock, Mockito.times(1)).deleteFireStation(any(FireStation.class));

        }

        @Test
        public void deleteNonExistingFireStationTest() {
            FireStation newFireStation = new FireStation();
            newFireStation.setAddress("25 rue de Nantes");
            newFireStation.setStation(12);

            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.deleteFireStation(newFireStation)).isFalse();
            verify(fireStationRepositoryMock, Mockito.times(0)).deleteFireStation(any(FireStation.class));
        }

        @Test
        public void deleteNullFireStationTest() {
            when(fireStationRepositoryMock.getFireStations()).thenReturn(fireStationList);
            assertThat(fireStationService.deleteFireStation(null)).isFalse();
            verify(fireStationRepositoryMock, Mockito.times(0)).deleteFireStation(any(FireStation.class));
        }
    }


}
