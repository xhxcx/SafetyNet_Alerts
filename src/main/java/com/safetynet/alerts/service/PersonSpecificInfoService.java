package com.safetynet.alerts.service;

import com.safetynet.alerts.model.dto.PersonInfoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonSpecificInfoService {

    List<PersonInfoDTO> getPersonInfo(String firstName, String lastName);
}
