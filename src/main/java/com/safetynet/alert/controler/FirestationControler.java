package com.safetynet.alert.controler;
import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.FireStationService;
import org.springframework.web.bind.annotation.*;

//TESTS
//import com.safetynet.alert.service.firestationServices;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//import com.safetynet.alert.service.dto.

@RestController
public class FirestationControler {

  private final FireStationService firestationService;

  public FirestationControler (FireStationService firestationService) {
    this.firestationService = firestationService;
  }


  // les Get's de FireStation
@GetMapping("firestations")
  public List<Firestation> allFireStation() {
    return firestationService.findAllFireStations ();
    }










}
