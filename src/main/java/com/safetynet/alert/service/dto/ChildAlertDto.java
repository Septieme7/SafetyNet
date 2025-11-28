package com.safetynet.alert.service.dto;


import com.safetynet.alert.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildAlertDto {
    private String firstName;
    private String lastName;
    private String age;
    private List<Person> household;




}
