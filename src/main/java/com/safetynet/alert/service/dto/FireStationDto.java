package com.safetynet.alert.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireStationDto {
    private String adult;
    private String child;
    private List<FireStationPersonDto> people;
}
