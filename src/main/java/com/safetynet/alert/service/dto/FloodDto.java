package com.safetynet.alert.service.dto;

import lombok.Builder;
import lombok.Data;


import java.util.List;
@Builder
@Data
public class FloodDto {
    private String address;
    private List<PersonDto>people;

    @Builder
    @Data
    public static class PersonDto {
        private String firstName;
        private String lastName;
        private String phone;
        private int age;
        private String[] medication;
        private String[] allergies;
    }

}

