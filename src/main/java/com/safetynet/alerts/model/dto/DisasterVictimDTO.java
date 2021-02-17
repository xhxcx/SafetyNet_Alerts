package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DisasterVictimDTO {

    private String lastName;

    private String phone;

    private int age;

    private List<String> medications;

    private List<String> allergies;
}
