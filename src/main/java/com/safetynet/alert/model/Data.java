package com.safetynet.alert.model;

import java.util.List;

@lombok.Data

public class Data {
    private List <Person> persons;
    private List <FireStation> firestations;
    private List <MedicalRecord> medicalrecords;

}
