package com.safetynet.alert.service.dto;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

  private final FireStationRepository fireStationRepository;

  public FireStationService (FireStationRepository fireStationRepository) {
    this.fireStationRepository = fireStationRepository;
  }


//  private final PersonRepository personRepository;
//  private final MedicalRecordsRepository medicalRecordsRepository;


public List<Firestation> findAllFireStations() {
  return fireStationRepository.findAllFireStations();
}
}


