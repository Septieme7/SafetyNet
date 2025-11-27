package com.safetynet.alert.service;

import com.safetynet.alert.model.Firestation;
import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.FireStationRepository;
import com.safetynet.alert.repository.MedicalRecordsRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertService {
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

  public FireDto getFireInfo(String address) {
    System.out.println("🔥 INFOS FEU POUR: " + address);

    // Trouver la station pour cette adresse
    String station = fireStationRepository.findAllFireStations().stream()
            .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
            .map(Firestation::getStation)
            .findFirst()
            .orElse(null);

    // Trouver les personnes à cette adresse
    List<Person> persons = personRepository.getAllPersons().stream()
            .filter(p -> p.getAddress().equalsIgnoreCase(address))
            .collect(Collectors.toList());

    // Construire les DTO des personnes
    List<FirePersonDto> personsDto = persons.stream().map(person -> {
      Medicalrecord medicalRecord = findMedicalRecord(person.getFirstName(), person.getLastName());

      FirePersonDto personDto = new FirePersonDto();
      personDto.setFirstName(person.getFirstName());
      personDto.setLastName(person.getLastName());
      personDto.setPhone(person.getPhone());

      if (medicalRecord != null) {
        personDto.setAge(medicalRecordsService.calculateAgeFromMedicalRecord(
                person.getFirstName(), person.getLastName()));
        personDto.setMedications(medicalRecord.getMedications() != null ?
                medicalRecord.getMedications() : new ArrayList<>());
        personDto.setAllergies(medicalRecord.getAllergies() != null ?
                medicalRecord.getAllergies() : new ArrayList<>());
      } else {
        personDto.setAge(0);
        personDto.setMedications(new ArrayList<>());
        personDto.setAllergies(new ArrayList<>());
      }
      return personDto;
    }).collect(Collectors.toList());

    FireDto result = new FireDto(station, personsDto);
    System.out.println("✅ " + personsDto.size() + " PERSONNE(S) TROUVÉE(S)");
    return result;
  }

  public Map<String, List<FloodPersonDto>> getFloodStations(List<String> stations) {
    System.out.println("🌊 STATIONS FLOOD: " + stations);

    Map<String, List<FloodPersonDto>> result = new HashMap<>();

    for (String station : stations) {
      // Trouver les adresses couvertes par cette station
      List<String> addresses = fireStationRepository.findAllFireStations().stream()
              .filter(fs -> fs.getStation().equals(station))
              .map(Firestation::getAddress)
              .collect(Collectors.toList());

      // Pour chaque adresse, récupérer les personnes
      for (String address : addresses) {
        List<FloodPersonDto> personsAtAddress = personRepository.getAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(person -> {
                  Medicalrecord medicalRecord = findMedicalRecord(person.getFirstName(), person.getLastName());

                  FloodPersonDto personDto = new FloodPersonDto();
                  personDto.setFirstName(person.getFirstName());
                  personDto.setLastName(person.getLastName());
                  personDto.setPhone(person.getPhone());

                  if (medicalRecord != null) {
                    personDto.setAge(medicalRecordsService.calculateAgeFromMedicalRecord(
                            person.getFirstName(), person.getLastName()));
                    personDto.setMedications(medicalRecord.getMedications() != null ?
                            medicalRecord.getMedications() : new ArrayList<>());
                    personDto.setAllergies(medicalRecord.getAllergies() != null ?
                            medicalRecord.getAllergies() : new ArrayList<>());
                  } else {
                    personDto.setAge(0);
                    personDto.setMedications(new ArrayList<>());
                    personDto.setAllergies(new ArrayList<>());
                  }
                  return personDto;
                })
                .collect(Collectors.toList());

        if (!personsAtAddress.isEmpty()) {
          result.put(address, personsAtAddress);
        }
      }
    }

    System.out.println("✅ DONNÉES FLOOD RÉCUPÉRÉES POUR " + result.size() + " ADRESSE(S)");
    return result;
  }

  public FireStationDto getFireStationCoverage(String stationNumber) {
    System.out.println("🏠 COUVERTURE STATION: " + stationNumber);

    // Adresses couvertes par la station
    List<String> addresses = fireStationRepository.findAllFireStations().stream()
            .filter(fs -> fs.getStation().equals(stationNumber))
            .map(Firestation::getAddress)
            .collect(Collectors.toList());

    // Personnes couvertes
    List<Person> allPersons = personRepository.getAllPersons().stream()
            .filter(p -> addresses.contains(p.getAddress()))
            .collect(Collectors.toList());

    // Compter adultes et enfants
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

    // Informations des personnes
    List<FireStationPersonDto> personsInfo = allPersons.stream()
            .map(person -> new FireStationPersonDto(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getPhone()
            ))
            .collect(Collectors.toList());

    FireStationDto result = new FireStationDto(stationNumber, adults, children, personsInfo);
    System.out.println("✅ " + adults + " ADULTE(S), " + children + " ENFANT(S)");
    return result;
  }

  public ChildAlertDto getChildAlert(String address) {
    System.out.println("👶 ALERTE ENFANTS ADRESSE: " + address);

    // Personnes à l'adresse
    List<Person> personsAtAddress = personRepository.getAllPersons().stream()
            .filter(p -> p.getAddress().equalsIgnoreCase(address))
            .collect(Collectors.toList());

    // Trouver les enfants
    List<Person> children = personsAtAddress.stream()
            .filter(person -> {
              int age = medicalRecordsService.calculateAgeFromMedicalRecord(
                      person.getFirstName(), person.getLastName());
              return age <= 18;
            })
            .collect(Collectors.toList());

    if (children.isEmpty()) {
      System.out.println("❌ AUCUN ENFANT TROUVÉ");
      return null;
    }

    // Pour chaque enfant, trouver les autres membres du foyer
    List<ChildAlertDto> childAlerts = children.stream().map(child -> {
      List<HouseholdMemberDto> householdMembers = personsAtAddress.stream()
              .filter(person -> !(person.getFirstName().equals(child.getFirstName()) &&
                      person.getLastName().equals(child.getLastName())))
              .map(person -> new HouseholdMemberDto(person.getFirstName(), person.getLastName()))
              .collect(Collectors.toList());

      int age = medicalRecordsService.calculateAgeFromMedicalRecord(
              child.getFirstName(), child.getLastName());

      return new ChildAlertDto(child.getFirstName(), child.getLastName(), age, householdMembers);
    }).collect(Collectors.toList());

    // Retourner le premier enfant trouvé (ou adapter selon besoins)
    ChildAlertDto result = childAlerts.get(0);
    System.out.println("✅ " + children.size() + " ENFANT(S) TROUVÉ(S)");
    return result;
  }

  private Medicalrecord findMedicalRecord(String firstName, String lastName) {
    return medicalRecordsRepository.getAllMedicalrecords().stream()
            .filter(mr -> mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName))
            .findFirst()
            .orElse(null);
  }
}