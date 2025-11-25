package com.safetynet.alert.controler;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.service.FireStationService;
import com.safetynet.alert.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FirestationControler {

  private final FireStationService firestationService;
  private final PersonService personService;

  public FirestationControler(FireStationService firestationService, PersonService personService) {
    this.firestationService = firestationService;
    this.personService = personService;
  }


    //  ENDPOINT 1 : Toutes les firestations
    @GetMapping
    public List<Firestation> getAllFireStations() {
      System.out.println(" Endpoint /firestations appelé");
      return firestationService.findAllFireStations();
    }

    //  ENDPOINT 2: Téléphones par numéro de caserne TOUS la 1,2,3,4,ect...
  // POSTMAN http://localhost:8080/firestation/phoneAlert?firestation=1
    // POSTMAN  http://localhost:8080/firestation/phoneAlert?firestation=2
    @GetMapping("firestation/phoneAlert")
    public List<String> getPhoneNumbersByStation(@RequestParam String firestation) {
      System.out.println("Endpoint /firestations/phoneAlert appelé avec firestation: " + firestation);
      return firestationService.findPhoneNumbersByStationsNumber(firestation);
    }

  @GetMapping("/cities")
  public List<String> getAllCities() {
    System.out.println("Endpoint GET /person/cities appelé");
    return personService.getAllCities();
  }
}
//   GET - Liste des personnes couvertes par une station
//  @GetMapping("/firestation")
//  public Map<String, Object> getPersonsByStation(@RequestParam String stationNumber) {
//    return firestationService.getPersonsByStation(stationNumber);
//  }



//  // POST - Ajouter une mapping station-adresse
//  @PostMapping("/firestation")
//  public Firestation addFirestation(@RequestBody Firestation firestation) {
//    return firestationService.addFirestation(firestation);
//  }
//
//  // PUT - Mettre à jour une station
//  @PutMapping("/firestation")
//  public Firestation updateFirestation(@RequestBody Firestation firestation) {
//    return firestationService.updateFirestation(firestation);
//  }
//
//  // DELETE - Supprimer une mapping station-adresse
//  @DeleteMapping("/firestation")
//  public void deleteFirestation(@RequestParam String address) {
//    firestationService.deleteFirestation(address);
//  }
//

//
// *********************************
//FAUX FAUX FAUX
// FAUX --> POSTMAN = http://localhost:8080/phoneAlert?firestation=1
//  @GetMapping("/phoneAlert")  //  Doit être présent
//  public List<String> getPhoneNumbersByStation(@RequestParam String firestation) {
//    return firestationService.findPhoneNumbersByStationsNumber(firestation);
//  }
// **********************************
//OK a décommenter pour utiliser C BON Ss7

//  // GET toutes les stations
//  @GetMapping("/firestations")
//  public List<Firestation> allFireStation() {
//    return firestationService.findAllFireStations();
//  }
//
//  // GET - Numéros de téléphone par caserne
//  @GetMapping("/phoneAlert")
//  public List<String> getPhonesByStation(@RequestParam String firestation) {
////    return firestationService.findPhoneNumbersByStation(firestation);
//    return firestationService.findPhoneNumbersByStationsNumber(firestation);
// **************************************
