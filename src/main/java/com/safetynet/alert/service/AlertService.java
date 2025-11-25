package com.safetynet.alert.service;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FireStationRepository;
import com.safetynet.alert.repository.MedicalRecordsRepository;
import com.safetynet.alert.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertService {

  private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

  private final PersonRepository personRepository;
  private final FireStationRepository fireStationRepository;
  private final MedicalRecordsRepository medicalRecordsRepository;
  private final MedicalRecordsService medicalRecordsService;

  public AlertService(PersonRepository personRepository,
                      FireStationRepository fireStationRepository,
                      MedicalRecordsRepository medicalRecordsRepository,
                      MedicalRecordsService medicalRecordsService) {
    this.personRepository = personRepository;
    this.fireStationRepository = fireStationRepository;
    this.medicalRecordsRepository = medicalRecordsRepository;
    this.medicalRecordsService = medicalRecordsService;
  }

  public Map<String, Object> getFireInfo(String address) {
    logger.info("Récupération des informations feu pour l'adresse: {}", address);

    Map<String, Object> result = new HashMap<>();

    // Trouver la station pour cette adresse
    String station = fireStationRepository.findAllFireStations().stream()
            .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
            .map(Firestation::getStation)
            .findFirst()
            .orElse(null);

    result.put("station", station);

    // Trouver les personnes à cette adresse
    List<Person> persons = personRepository.getAllPersons().stream()
            .filter(p -> p.getAddress().equalsIgnoreCase(address))
            .collect(Collectors.toList());

    List<Map<String, Object>> personsInfo = persons.stream().map(person -> {
      Map<String, Object> personInfo = new HashMap<>();
      personInfo.put("firstName", person.getFirstName());
      personInfo.put("lastName", person.getLastName());
      personInfo.put("phone", person.getPhone());

      // Calculer l'âge et récupérer les infos médicales
      Medicalrecord medicalRecord = findMedicalRecord(person.getFirstName(), person.getLastName());
      if (medicalRecord != null) {
        personInfo.put("age", medicalRecordsService.calculateAgeFromMedicalRecord(
                person.getFirstName(), person.getLastName()));
        personInfo.put("medications",
                medicalRecord.getMedications() != null ? medicalRecord.getMedications() : new String[0]);
        personInfo.put("allergies",
                medicalRecord.getAllergies() != null ? medicalRecord.getAllergies() : new String[0]);
      } else {
        personInfo.put("age", 0);
        personInfo.put("medications", new String[0]);
        personInfo.put("allergies", new String[0]);
      }

      return personInfo;
    }).collect(Collectors.toList());

    result.put("persons", personsInfo);
    logger.info("Informations feu récupérées: {} personnes trouvées", personsInfo.size());
    return result;
  }

  public Map<String, Object> getFloodStations(List<String> stations) {
    logger.info("Récupération des informations flood pour les stations: {}", stations);

    Map<String, Object> result = new HashMap<>();

    for (String station : stations) {
      // Trouver les adresses couvertes par cette station
      List<String> addresses = fireStationRepository.findAllFireStations().stream()
              .filter(fs -> fs.getStation().equals(station))
              .map(Firestation::getAddress)
              .collect(Collectors.toList());

      Map<String, List<Map<String, Object>>> stationInfo = new HashMap<>();

      for (String address : addresses) {
        List<Map<String, Object>> personsAtAddress = personRepository.getAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(person -> {
                  Map<String, Object> personInfo = new HashMap<>();
                  personInfo.put("firstName", person.getFirstName());
                  personInfo.put("lastName", person.getLastName());
                  personInfo.put("phone", person.getPhone());

                  Medicalrecord medicalRecord = findMedicalRecord(person.getFirstName(), person.getLastName());
                  if (medicalRecord != null) {
                    personInfo.put("age", medicalRecordsService.calculateAgeFromMedicalRecord(
                            person.getFirstName(), person.getLastName()));
                    personInfo.put("medications",
                            medicalRecord.getMedications() != null ? medicalRecord.getMedications() : new String[0]);
                    personInfo.put("allergies",
                            medicalRecord.getAllergies() != null ? medicalRecord.getAllergies() : new String[0]);
                  } else {
                    personInfo.put("age", 0);
                    personInfo.put("medications", new String[0]);
                    personInfo.put("allergies", new String[0]);
                  }

                  return personInfo;
                })
                .collect(Collectors.toList());

        if (!personsAtAddress.isEmpty()) {
          stationInfo.put(address, personsAtAddress);
        }
      }

      if (!stationInfo.isEmpty()) {
        result.put("station_" + station, stationInfo);
      }
    }

    logger.info("Informations flood récupérées pour {} stations", result.size());
    return result;
  }

  public Map<String, Object> getFireStationCoverage(String stationNumber) {
    logger.info("Récupération de la couverture de la station: {}", stationNumber);

    Map<String, Object> result = new HashMap<>();

    // Trouver les adresses couvertes par cette station
    List<String> addresses = fireStationRepository.findAllFireStations().stream()
            .filter(fs -> fs.getStation().equals(stationNumber))
            .map(Firestation::getAddress)
            .collect(Collectors.toList());

    // Compter adultes et enfants
    List<Person> allPersons = personRepository.getAllPersons().stream()
            .filter(p -> addresses.contains(p.getAddress()))
            .collect(Collectors.toList());

    long adults = allPersons.stream()
            .filter(person -> {
              int age = medicalRecordsService.calculateAgeFromMedicalRecord(
                      person.getFirstName(), person.getLastName());
              return age > 18;
            })
            .count();

    long children = allPersons.stream()
            .filter(person -> {
              int age = medicalRecordsService.calculateAgeFromMedicalRecord(
                      person.getFirstName(), person.getLastName());
              return age <= 18;
            })
            .count();

    // Préparer les informations des personnes
    List<Map<String, String>> personsInfo = allPersons.stream()
            .map(person -> {
              Map<String, String> info = new HashMap<>();
              info.put("firstName", person.getFirstName());
              info.put("lastName", person.getLastName());
              info.put("address", person.getAddress());
              info.put("phone", person.getPhone());
              return info;
            })
            .collect(Collectors.toList());

    result.put("station", stationNumber);
    result.put("adults", adults);
    result.put("children", children);
    result.put("persons", personsInfo);

    logger.info("Couverture station {}: {} adultes, {} enfants, {} personnes",
            stationNumber, adults, children, personsInfo.size());
    return result;
  }

  private Medicalrecord findMedicalRecord(String firstName, String lastName) {
    return medicalRecordsRepository.getAllMedicalrecords().stream()
            .filter(mr -> mr.getFirstName().equals(firstName)
                    && mr.getLastName().equals(lastName))
            .findFirst()
            .orElse(null);
  }
}