package com.safetynet.alert.service;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FireStationRepository;
import com.safetynet.alert.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationService {

  private final FireStationRepository fireStationRepository;
  private final PersonRepository personRepository;

  public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository) {
    this.fireStationRepository = fireStationRepository;
    this.personRepository = personRepository;
  }

  public List<Firestation> findAllFireStations() {
    return fireStationRepository.findAllFireStations();
  }

  public List<String> findPhoneNumbersByStationsNumber(String number) {
    // String au lieu de int
    List<String> result = new ArrayList<>();

    // Récupère TOUTES les firestations
    List<Firestation> allFirestations = fireStationRepository.findAllFireStations();

    // Filtre les firestations qui correspondent au numéro demandé
    List<Firestation> filteredFirestations = new ArrayList<>();
    for (Firestation firestation : allFirestations) {
      //  Utilise .equals() pour comparer des String
      if (number != null && number.equals(firestation.getStation())) {
        filteredFirestations.add(firestation);
      }
    }

    // Récupère toutes les personnes
    List<Person> persons = personRepository.getAllPersons();

    // Compare les adresses des personnes avec celles des firestations filtrées
    for (Person person : persons) {
      for (Firestation firestation : filteredFirestations) {
        if (person.getAddress() != null &&
                person.getAddress().equals(firestation.getAddress())) {
          result.add(person.getPhone());
          break; // Évite d'ajouter plusieurs fois le même téléphone
        }
      }
    }
// retour des numero de tel des casernes
    return result;
  }

}