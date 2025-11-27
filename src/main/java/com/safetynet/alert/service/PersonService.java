package com.safetynet.alert.service;

import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.MedicalRecordsRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.service.dto.ChildAlertDto;
import com.safetynet.alert.service.dto.HouseholdMemberDto;
import com.safetynet.alert.service.dto.PersonInfoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service contenant la logique métier pour les opérations sur les personnes
 * Gère la création, lecture, mise à jour et suppression des personnes
 */
@Service
public class PersonService {

  // === DÉPENDANCES REPOSITORY ===
  private final PersonRepository personRepository;
  private final MedicalRecordsRepository medicalRecordsRepository;

  /**
   * Constructeur avec injection des dépendances
   * Spring injecte automatiquement les repositories
   */
  public PersonService(PersonRepository personRepository, MedicalRecordsRepository medicalRecordsRepository) {
    this.personRepository = personRepository;
    this.medicalRecordsRepository = medicalRecordsRepository;
  }

  // === MÉTHODES DE LECTURE ===

  /**
   * Récupère toutes les personnes du système
   * @return Liste de toutes les personnes
   */
  public List<Person> getAllPersons() {
    System.out.println("👥 RÉCUPÉRATION DE TOUTES LES PERSONNES");
    List<Person> persons = personRepository.getAllPersons();
    System.out.println("✅ " + persons.size() + " PERSONNE(S) TROUVÉE(S)");
    return persons;
  }

  /**
   * Récupère tous les emails des personnes d'une ville spécifique
   * @param city Ville pour filtrer les emails
   * @return Liste des emails des personnes de cette ville
   */
  public List<String> findAllEmailsByCity(String city) {
    System.out.println("📧 RECHERCHE EMAILS POUR LA VILLE : " + city);

    List<String> emails = new ArrayList<>();
    List<Person> persons = personRepository.getAllPersons();

    for (Person person : persons) {
      if (person.getCity().equals(city)) {
        emails.add(person.getEmail());
      }
    }

    System.out.println("✅ " + emails.size() + " EMAIL(S) TROUVÉ(S) POUR " + city);
    return emails;
  }

  /**
   * Récupère les informations complètes d'une personne
   * @param firstName Prénom de la personne recherchée
   * @param lastName Nom de famille de la personne recherchée
   * @return DTO avec toutes les informations ou null si non trouvée
   */
  public PersonInfoDto getPersonInfoDtoList(String firstName, String lastName) {
    System.out.println("👤 RECHERCHE INFOS PERSONNE : " + firstName + " " + lastName);

    // Récupère toutes les données nécessaires
    List<Person> personList = personRepository.getAllPersons();
    List<Medicalrecord> medicalrecords = medicalRecordsRepository.getAllMedicalrecords();

    // ÉTAPE 1: RECHERCHE DE LA PERSONNE
    Person foundPerson = null;
    for (Person person : personList) {
      // Compare prénom et nom (insensible à la casse)
      if (person.getFirstName().equalsIgnoreCase(firstName) &&
              person.getLastName().equalsIgnoreCase(lastName)) {
        foundPerson = person;  // Personne trouvée
        break;
      }
    }

    // Si personne non trouvée, retourne null
    if (foundPerson == null) {
      System.out.println("❌ PERSONNE NON TROUVÉE : " + firstName + " " + lastName);
      return null;
    }

    // ÉTAPE 2: RECHERCHE DU DOSSIER MÉDICAL
    Medicalrecord foundMedicalRecord = null;
    for (Medicalrecord medicalrecord : medicalrecords) {
      // Compare prénom et nom (insensible à la casse)
      if (medicalrecord.getFirstName().equalsIgnoreCase(firstName) &&
              medicalrecord.getLastName().equalsIgnoreCase(lastName)) {
        foundMedicalRecord = medicalrecord;  // Dossier médical trouvé
        break;
      }
    }

    // ÉTAPE 3: CRÉATION DU DTO AVEC TOUS LES CHAMPS
    PersonInfoDto personInfoDto = new PersonInfoDto();

    // Remplit toutes les informations personnelles
    personInfoDto.setFirstName(foundPerson.getFirstName());
    personInfoDto.setLastName(foundPerson.getLastName());
    personInfoDto.setAddress(foundPerson.getAddress());
    personInfoDto.setCity(foundPerson.getCity());
    personInfoDto.setZip(foundPerson.getZip());
    personInfoDto.setPhone(foundPerson.getPhone());
    personInfoDto.setEmail(foundPerson.getEmail());

    // ÉTAPE 4: TRAITEMENT DES INFORMATIONS MÉDICALES
    if (foundMedicalRecord != null) {
      // Calcule l'âge à partir de la date de naissance
      int age = calculateAge(foundMedicalRecord.getBirthdate());
      personInfoDto.setAge(age);

      // ✅ UTILISATION DIRECTE DES LIST - PLUS BESOIN DE CONVERSION
      personInfoDto.setMedications(foundMedicalRecord.getMedications());
      personInfoDto.setAllergies(foundMedicalRecord.getAllergies());
    } else {
      // Valeurs par défaut si pas de dossier médical
      personInfoDto.setAge(0);
      personInfoDto.setMedications(new ArrayList<>());
      personInfoDto.setAllergies(new ArrayList<>());
    }

    System.out.println("✅ INFOS PERSONNE RÉCUPÉRÉES : " + firstName + " " + lastName);
    return personInfoDto;
  }

  /**
   * Récupère les enfants habitant à une adresse avec les membres du foyer
   * @param address Adresse pour la recherche d'enfants
   * @return ChildAlertDto avec les enfants et membres du foyer ou null si aucun enfant
   */
  public ChildAlertDto getChildAlert(String address) {
    System.out.println("👶 RECHERCHE ENFANTS POUR L'ADRESSE : " + address);

    // Récupère toutes les personnes à cette adresse
    List<Person> personsAtAddress = personRepository.getAllPersons().stream()
            .filter(person -> person.getAddress().equalsIgnoreCase(address))
            .collect(Collectors.toList());

    if (personsAtAddress.isEmpty()) {
      System.out.println("❌ AUCUNE PERSONNE TROUVÉE À CETTE ADRESSE : " + address);
      return null;
    }

    System.out.println("🔍 " + personsAtAddress.size() + " PERSONNE(S) TROUVÉE(S) À CETTE ADRESSE");

    // Trouve les enfants (<= 18 ans) avec leurs âges
    List<ChildInfo> children = new ArrayList<>();
    for (Person person : personsAtAddress) {
      int age = calculateAgeFromPerson(person);
      if (age <= 18) {
        children.add(new ChildInfo(person, age));
        System.out.println("🎯 ENFANT TROUVÉ : " + person.getFirstName() + " " + person.getLastName() + " (" + age + " ans)");
      }
    }

    if (children.isEmpty()) {
      System.out.println("❌ AUCUN ENFANT (<= 18 ans) TROUVÉ À L'ADRESSE : " + address);
      return null;
    }

    System.out.println("✅ " + children.size() + " ENFANT(S) TROUVÉ(S) AU TOTAL");

    // Pour le premier enfant trouvé, crée le DTO avec les autres membres du foyer
    ChildInfo firstChild = children.get(0);

    // Crée la liste des autres membres du foyer (exclut l'enfant)
    List<HouseholdMemberDto> householdMembers = personsAtAddress.stream()
            .filter(person -> !(person.getFirstName().equals(firstChild.person.getFirstName()) &&
                    person.getLastName().equals(firstChild.person.getLastName())))
            .map(person -> new HouseholdMemberDto(person.getFirstName(), person.getLastName()))
            .collect(Collectors.toList());

    ChildAlertDto childAlert = new ChildAlertDto(
            firstChild.person.getFirstName(),
            firstChild.person.getLastName(),
            firstChild.age,
            householdMembers
    );

    System.out.println("✅ ALERTE ENFANTS CRÉÉE : " + firstChild.person.getFirstName() + " " +
            firstChild.person.getLastName() + " (" + firstChild.age + " ans) avec " +
            householdMembers.size() + " membre(s) du foyer");
    return childAlert;
  }

  /**
   * Récupère la liste de toutes les villes uniques
   * @return Liste des villes sans doublons
   */
  public List<String> getAllCities() {
    System.out.println("🏙️ RÉCUPÉRATION DE TOUTES LES VILLES");

    List<Person> persons = personRepository.getAllPersons();
    List<String> cities = new ArrayList<>();

    for (Person person : persons) {
      String city = person.getCity();
      if (city != null && !city.isEmpty() && !cities.contains(city)) {
        cities.add(city);  // Ajoute seulement si pas déjà présent
      }
    }

    System.out.println("✅ " + cities.size() + " VILLE(S) TROUVÉE(S) : " + cities);
    return cities;
  }

  // === MÉTHODES D'ÉCRITURE (CRÉATION, MISE À JOUR, SUPPRESSION) ===

  /**
   * Crée une nouvelle personne
   * @param person Personne à créer
   * @return Personne créée
   */
  public Person createPerson(Person person) {
    System.out.println("➕ CRÉATION D'UNE NOUVELLE PERSONNE : " +
            person.getFirstName() + " " + person.getLastName());

    // Récupère la liste actuelle des personnes
    List<Person> persons = personRepository.getAllPersons();

    // Vérifie si la personne existe déjà
    boolean personExists = persons.stream()
            .anyMatch(p -> p.getFirstName().equals(person.getFirstName()) &&
                    p.getLastName().equals(person.getLastName()));

    if (personExists) {
      System.out.println("❌ PERSONNE EXISTE DÉJÀ : " + person.getFirstName() + " " + person.getLastName());
      throw new RuntimeException("La personne existe déjà");
    }

    // Ajoute la nouvelle personne à la liste
    persons.add(person);

    System.out.println("✅ PERSONNE CRÉÉE AVEC SUCCÈS : " + person.getFirstName() + " " + person.getLastName());
    return person;
  }

  /**
   * Met à jour une personne existante
   * @param updatedPerson Personne avec les nouvelles données
   * @return Personne mise à jour ou null si non trouvée
   */
  public Person updatePerson(Person updatedPerson) {
    System.out.println("✏️  MISE À JOUR PERSONNE : " +
            updatedPerson.getFirstName() + " " + updatedPerson.getLastName());

    // Récupère la liste actuelle des personnes
    List<Person> persons = personRepository.getAllPersons();

    // Parcourt la liste pour trouver la personne à mettre à jour
    for (int i = 0; i < persons.size(); i++) {
      Person existingPerson = persons.get(i);

      // Vérifie si c'est la bonne personne (par prénom et nom)
      if (existingPerson.getFirstName().equals(updatedPerson.getFirstName()) &&
              existingPerson.getLastName().equals(updatedPerson.getLastName())) {

        // Remplace l'ancienne personne par la nouvelle version
        persons.set(i, updatedPerson);

        System.out.println("✅ PERSONNE MISE À JOUR AVEC SUCCÈS : " +
                updatedPerson.getFirstName() + " " + updatedPerson.getLastName());
        return updatedPerson;
      }
    }

    System.out.println("❌ PERSONNE NON TROUVÉE POUR MISE À JOUR : " +
            updatedPerson.getFirstName() + " " + updatedPerson.getLastName());
    return null;
  }

  /**
   * Supprime une personne
   * @param firstName Prénom de la personne à supprimer
   * @param lastName Nom de famille de la personne à supprimer
   * @return true si supprimée, false si non trouvée
   */
  public boolean deletePerson(String firstName, String lastName) {
    System.out.println("🗑️  SUPPRESSION PERSONNE : " + firstName + " " + lastName);

    // Récupère la liste actuelle des personnes
    List<Person> persons = personRepository.getAllPersons();

    // Parcourt la liste pour trouver la personne à supprimer
    for (int i = 0; i < persons.size(); i++) {
      Person person = persons.get(i);

      // Vérifie si c'est la bonne personne
      if (person.getFirstName().equals(firstName) &&
              person.getLastName().equals(lastName)) {

        // Supprime la personne de la liste
        persons.remove(i);

        System.out.println("✅ PERSONNE SUPPRIMÉE AVEC SUCCÈS : " + firstName + " " + lastName);
        return true;
      }
    }

    System.out.println("❌ PERSONNE NON TROUVÉE POUR SUPPRESSION : " + firstName + " " + lastName);
    return false;
  }

  // === MÉTHODES UTILITAIRES ===

  /**
   * Calcule l'âge d'une personne à partir de son dossier médical (méthode publique)
   * @param person Personne pour laquelle calculer l'âge
   * @return Âge en années
   */
  public int calculateAgeFromPerson(Person person) {
    System.out.println("📊 CALCUL ÂGE POUR : " + person.getFirstName() + " " + person.getLastName());

    // Recherche le dossier médical de la personne
    Medicalrecord medicalRecord = medicalRecordsRepository.getAllMedicalrecords().stream()
            .filter(mr -> mr.getFirstName().equals(person.getFirstName()) &&
                    mr.getLastName().equals(person.getLastName()))
            .findFirst()
            .orElse(null);

    if (medicalRecord != null && medicalRecord.getBirthdate() != null) {
      int age = calculateAge(medicalRecord.getBirthdate());
      System.out.println("✅ ÂGE CALCULÉ : " + age + " ans");
      return age;
    }

    System.out.println("❌ DOSSIER MÉDICAL NON TROUVÉ POUR LE CALCUL D'ÂGE");
    return 0; // Retourne 0 si pas de dossier médical trouvé
  }

  /**
   * Calcule l'âge à partir d'une date de naissance
   * @param birthdate Date de naissance au format "MM/dd/yyyy"
   * @return Âge en années
   */
  private int calculateAge(String birthdate) {
    try {
      // Définit le format de date attendu
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

      // Convertit la String en LocalDate
      LocalDate birthDate = LocalDate.parse(birthdate, formatter);

      // Récupère la date actuelle
      LocalDate currentDate = LocalDate.now();

      // Calcule la différence en années
      Period period = Period.between(birthDate, currentDate);

      return period.getYears();

    } catch (Exception e) {
      // En cas d'erreur, log et retourne 0
      System.err.println("❌ ERREUR CALCUL ÂGE : " + birthdate);
      e.printStackTrace();
      return 0;
    }
  }

  // === CLASSE INTERNE POUR STOCKER LES INFOS ENFANTS ===

  /**
   * Classe interne pour stocker les informations d'un enfant avec son âge
   */
  private static class ChildInfo {
    Person person;
    int age;

    ChildInfo(Person person, int age) {
      this.person = person;
      this.age = age;
    }
  }
}