package com.safetynet.alert.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireStationPersonDto {
        private String firstName;
        private String lastName;
        private String phone;
        private String address;
}
