package com.safetynet.alert.service;

import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.MedicalRecordsRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.service.dto.PersonInfoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service contenant la logique métier pour les opérations sur les personnes
 */
@Service
public class PersonService {
  private final PersonRepository personRepository;
  private final MedicalRecordsRepository medicalRecordsRepository;

  public PersonService(PersonRepository personRepository, MedicalRecordsRepository medicalRecordsRepository) {
    this.personRepository = personRepository;
    this.medicalRecordsRepository = medicalRecordsRepository;
  }

  /**
   * Récupère tous les emails des personnes d'une ville spécifique
   */
  public List<String> findAllEmailsByCity(String city) {
    System.out.println("📧 RECHERCHE EMAILS POUR: " + city);
    List<String> emails = new ArrayList<>();
    List<Person> persons = personRepository.getAllPersons();

    for (Person person : persons) {
      if (person.getCity().equals(city)) {
        emails.add(person.getEmail());
      }
    }
    System.out.println("✅ " + emails.size() + " EMAIL(S) TROUVÉ(S)");
    return emails;
  }

  /**
   * Récupère les informations complètes d'une personne
   */
  public PersonInfoDto getPersonInfoDtoList(String firstName, String lastName) {
    System.out.println("👤 RECHERCHE INFOS: " + firstName + " " + lastName);

    // Recherche de la personne
    Person foundPerson = null;
    for (Person person : personRepository.getAllPersons()) {
      if (person.getFirstName().equalsIgnoreCase(firstName) &&
              person.getLastName().equalsIgnoreCase(lastName)) {
        foundPerson = person;
        break;
      }
    }

    if (foundPerson == null) {
      System.out.println("❌ PERSONNE NON TROUVÉE");
      return null;
    }

    // Recherche du dossier médical
    Medicalrecord foundMedicalRecord = null;
    for (Medicalrecord medicalrecord : medicalRecordsRepository.getAllMedicalrecords()) {
      if (medicalrecord.getFirstName().equalsIgnoreCase(firstName) &&
              medicalrecord.getLastName().equalsIgnoreCase(lastName)) {
        foundMedicalRecord = medicalrecord;
        break;
      }
    }

    // Création du DTO avec toutes les informations
    PersonInfoDto dto = new PersonInfoDto();
    dto.setFirstName(foundPerson.getFirstName());
    dto.setLastName(foundPerson.getLastName());
    dto.setAddress(foundPerson.getAddress());
    dto.setCity(foundPerson.getCity());
    dto.setZip(foundPerson.getZip());
    dto.setPhone(foundPerson.getPhone());
    dto.setEmail(foundPerson.getEmail());

    // Traitement des informations médicales
    if (foundMedicalRecord != null) {
      int age = calculateAge(foundMedicalRecord.getBirthdate());
      dto.setAge(age);
      dto.setMedications(foundMedicalRecord.getMedications() != null ?
              foundMedicalRecord.getMedications().toArray(new String[0]) : new String[0]);
      dto.setAllergies(foundMedicalRecord.getAllergies() != null ?
              foundMedicalRecord.getAllergies().toArray(new String[0]) : new String[0]);
    } else {
      dto.setAge(0);
      dto.setMedications(new String[0]);
      dto.setAllergies(new String[0]);
    }

    System.out.println("✅ INFOS RÉCUPÉRÉES");
    return dto;
  }

  /**
   * Crée une nouvelle personne
   */
  public Person createPerson(Person person) {
    System.out.println("➕ CRÉATION: " + person.getFirstName() + " " + person.getLastName());
    List<Person> persons = personRepository.getAllPersons();
    persons.add(person);
    System.out.println("✅ PERSONNE CRÉÉE");
    return person;
  }

  /**
   * Met à jour une personne existante
   */
  public Person updatePerson(Person updatedPerson) {
    System.out.println("✏️  MISE À JOUR: " + updatedPerson.getFirstName() + " " + updatedPerson.getLastName());
    List<Person> persons = personRepository.getAllPersons();

    for (int i = 0; i < persons.size(); i++) {
      Person existingPerson = persons.get(i);
      if (existingPerson.getFirstName().equals(updatedPerson.getFirstName()) &&
              existingPerson.getLastName().equals(updatedPerson.getLastName())) {
        persons.set(i, updatedPerson);
        System.out.println("✅ PERSONNE MISE À JOUR");
        return updatedPerson;
      }
    }

    System.out.println("❌ PERSONNE NON TROUVÉE POUR MISE À JOUR");
    return null;
  }

  /**
   * Supprime une personne
   */
  public boolean deletePerson(String firstName, String lastName) {
    System.out.println("🗑️  SUPPRESSION: " + firstName + " " + lastName);
    List<Person> persons = personRepository.getAllPersons();

    for (int i = 0; i < persons.size(); i++) {
      Person person = persons.get(i);
      if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
        persons.remove(i);
        System.out.println("✅ PERSONNE SUPPRIMÉE");
        return true;
      }
    }

    System.out.println("❌ PERSONNE NON TROUVÉE POUR SUPPRESSION");
    return false;
  }

  /**
   * Récupère la liste de toutes les villes uniques
   */
  public List<String> getAllCities() {
    List<Person> persons = personRepository.getAllPersons();
    List<String> cities = new ArrayList<>();

    for (Person person : persons) {
      String city = person.getCity();
      if (city != null && !cities.contains(city)) {
        cities.add(city);
      }
    }

    System.out.println("🏙️ " + cities.size() + " VILLES TROUVÉES");
    return cities;
  }

  /**
   * Calcule l'âge à partir d'une date de naissance
   */
  private int calculateAge(String birthdate) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      LocalDate birthDate = LocalDate.parse(birthdate, formatter);
      return Period.between(birthDate, LocalDate.now()).getYears();
    } catch (Exception e) {
      System.err.println("❌ ERREUR CALCUL ÂGE: " + birthdate);
      return 0;
    }
  }
}
