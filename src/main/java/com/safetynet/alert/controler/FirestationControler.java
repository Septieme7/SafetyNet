package com.safetynet.alert.controler;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.FireStationService;
import com.safetynet.alert.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour les opérations sur les casernes
 */
@RestController
public class FirestationControler {
  private final FireStationService firestationService;
  private final PersonService personService;

  public FirestationControler(FireStationService firestationService, PersonService personService) {
    this.firestationService = firestationService;
    this.personService = personService;
  }

  /**
   * Récupère toutes les casernes
   */
  @GetMapping("/firestations")
  public List<Firestation> getAllFireStations() {
    System.out.println("🚒 TOUTES LES STATIONS");
    return firestationService.findAllFireStations();
  }

  /**
   * Récupère toutes les villes
   */
  @GetMapping("/cities")
  public List<String> getAllCities() {
    System.out.println("🏙️ TOUTES LES VILLES");
    return personService.getAllCities();
  }
}
