package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChildAlertDTO {

    private List<ChildDTO> childList;

    private List<FamilyMemberDTO> familyMemberList;

}
