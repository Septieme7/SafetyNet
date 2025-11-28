package com.safetynet.alert.service.dto;


import lombok.Data;

@Data
public class FireDto {

    private String station;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String age;
    private String[] medications;
    private String[] allergies;
}
