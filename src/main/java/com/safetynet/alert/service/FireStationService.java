package com.safetynet.alert.service;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FireStationRepository;
import com.safetynet.alert.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service pour les opérations sur les casernes de pompiers
 */
@Service
public class FireStationService {
  private final FireStationRepository fireStationRepository;
  private final PersonRepository personRepository;

  public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository) {
    this.fireStationRepository = fireStationRepository;
    this.personRepository = personRepository;
  }

  /**
   * Récupère toutes les casernes
   */
  public List<Firestation> findAllFireStations() {
    System.out.println("🚒 RÉCUPÉRATION DE TOUTES LES STATIONS");
    return fireStationRepository.findAllFireStations();
  }

  /**
   * Trouve les numéros de téléphone par numéro de station
   */
  public List<String> findPhoneNumbersByStationsNumber(String number) {
    System.out.println("📞 RECHERCHE TÉLÉPHONES STATION: " + number);
    List<String> result = new ArrayList<>();
    List<Firestation> allFirestations = fireStationRepository.findAllFireStations();
    List<Firestation> filteredFirestations = new ArrayList<>();

    // Filtrage des stations par numéro
    for (Firestation firestation : allFirestations) {
      if (number != null && number.equals(firestation.getStation())) {
        filteredFirestations.add(firestation);
      }
    }

    // Recherche des personnes aux adresses couvertes
    List<Person> persons = personRepository.getAllPersons();
    for (Person person : persons) {
      for (Firestation firestation : filteredFirestations) {
        if (person.getAddress() != null && person.getAddress().equals(firestation.getAddress())) {
          result.add(person.getPhone());
          break;
        }
      }
    }

    System.out.println("✅ " + result.size() + " NUMÉRO(S) TROUVÉ(S)");
    return result;
  }
}
