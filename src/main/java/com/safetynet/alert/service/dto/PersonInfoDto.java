package com.safetynet.alert.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfoDto {

    private String lastName;
    private String email;
    private String age;
    private String address;
    private String[] medications;
    private String[] allergies;

}
