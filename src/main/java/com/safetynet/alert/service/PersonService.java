package com.safetynet.alert.service;

// === IMPORTS ===
import com.safetynet.alert.model.Firestation;          // Modèle Firestation
import com.safetynet.alert.model.Medicalrecord;        // Modèle Medicalrecord
import com.safetynet.alert.model.Person;               // Modèle Person
import com.safetynet.alert.repository.FireStationRepository;        // Repository Firestation
import com.safetynet.alert.repository.MedicalRecordsRepository;    // Repository Medicalrecord
import com.safetynet.alert.repository.PersonRepository;            // Repository Person
import com.safetynet.alert.service.dto.PersonInfoDto;              // DTO PersonInfo
import org.springframework.stereotype.Service;                     // Annotation Service

import java.time.LocalDate;            // Pour manipulation dates
import java.time.Period;               // Pour calcul périodes
import java.time.format.DateTimeFormatter; // Pour formatage dates
import java.util.ArrayList;            // Pour listes dynamiques
import java.util.List;                 // Interface List

/**
 * Service contenant la logique métier pour les opérations sur les personnes
 * Gère la création, lecture, mise à jour et suppression des personnes
 */
@Service  // Déclare cette classe comme Service Spring
public class PersonService {

  // === DÉPENDANCES REPOSITORY ===
  private final PersonRepository personRepository;                    // Accès aux personnes
  private final FireStationRepository firestationRepository;         // Accès aux casernes
  private final MedicalRecordsRepository medicalRecordsRepository;   // Accès aux dossiers médicaux

  /**
   * Constructeur avec injection des dépendances
   * Spring injecte automatiquement les repositories
   */
  public PersonService(PersonRepository personRepository, FireStationRepository firestationRepository, MedicalRecordsRepository medicalRecordsRepository) {
    this.personRepository = personRepository;
    this.firestationRepository = firestationRepository;
    this.medicalRecordsRepository = medicalRecordsRepository;
  }

  // === MÉTHODES DE LECTURE ===

  /**
   * Récupère tous les emails des personnes d'une ville spécifique
   * @param city Ville pour filtrer les emails
   * @return Liste des emails des personnes de cette ville
   */
  public List<String> findAllEmailsByCity(String city) {
    // Crée une liste vide pour stocker les emails
    List<String> emails = new ArrayList<>();

    // Récupère toutes les personnes du système
    List<Person> persons = personRepository.getAllPersons();

    // Parcourt chaque personne
    for (Person person : persons) {
      // Vérifie si la personne habite dans la ville recherchée
      if (person.getCity().equals(city)) {
        // Ajoute l'email à la liste résultat
        emails.add(person.getEmail());
      }
    }

    // Retourne la liste des emails trouvés
    return emails;
  }

  /**
   * Trouve les numéros de téléphone des personnes couvertes par une caserne
   * @param number Numéro de la caserne
   * @return Liste des numéros de téléphone
   */
  public List<String> findPhoneByNumber(String number) {
    // Liste pour stocker les numéros de téléphone résultat
    List<String> phones = new ArrayList<>();

    // Récupère toutes les personnes
    List<Person> persons = personRepository.getAllPersons();

    // Récupère toutes les casernes
    List<Firestation> firestations = firestationRepository.findAllFireStations();

    // Liste pour stocker les casernes filtrées par numéro
    List<Firestation> sortedFirestation = new ArrayList<>();

    // Étape 1: Filtrer les casernes par numéro
    for (Firestation firestation : firestations) {
      // Vérifie si la caserne a le numéro recherché
      if (firestation.getStation().equals(number)) {
        // Ajoute la caserne à la liste filtrée
        sortedFirestation.add(firestation);
      }
    }

    // Étape 2: Trouver les personnes vivant aux adresses couvertes par ces casernes
    for (Person person : persons) {
      for (Firestation firestation : sortedFirestation) {
        // Vérifie si la personne habite à une adresse couverte par la caserne
        if (person.getAddress().equals(firestation.getAddress())) {
          // Ajoute le numéro de téléphone à la liste résultat
          phones.add(person.getPhone());
          break;  // Sort de la boucle interne pour éviter les doublons
        }
      }
    }

    return phones;
  }

  /**
   * Récupère les informations complètes d'une personne
   * @param firstName Prénom de la personne recherchée
   * @param lastName Nom de famille de la personne recherchée
   * @return DTO avec toutes les informations ou null si non trouvée
   */
  public PersonInfoDto getPersonInfoDtoList(String firstName, String lastName) {
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
        break;  // Sort de la boucle
      }
    }

    // Si personne non trouvée, retourne null
    if (foundPerson == null) {
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

    // Remplit toutes les informations personnelles (CORRIGÉ)
    personInfoDto.setFirstName(foundPerson.getFirstName());  // ← CHAMP AJOUTÉ
    personInfoDto.setLastName(foundPerson.getLastName());
    personInfoDto.setAddress(foundPerson.getAddress());
    personInfoDto.setCity(foundPerson.getCity());            // ← CHAMP AJOUTÉ
    personInfoDto.setZip(foundPerson.getZip());              // ← CHAMP AJOUTÉ
    personInfoDto.setPhone(foundPerson.getPhone());          // ← CHAMP AJOUTÉ
    personInfoDto.setEmail(foundPerson.getEmail());

    // ÉTAPE 4: TRAITEMENT DES INFORMATIONS MÉDICALES
    if (foundMedicalRecord != null) {
      // Calcule l'âge à partir de la date de naissance
      int age = calculateAge(foundMedicalRecord.getBirthdate());
      personInfoDto.setAge(age);  // ← CORRIGÉ : setAge(age) directement

      // Convertit les médicaments List<String> en String[]
      if (foundMedicalRecord.getMedications() != null) {
        String[] medsArray = foundMedicalRecord.getMedications().toArray(new String[0]);
        personInfoDto.setMedications(medsArray);
      } else {
        personInfoDto.setMedications(new String[0]);  // Tableau vide
      }

      // Convertit les allergies List<String> en String[]
      if (foundMedicalRecord.getAllergies() != null) {
        String[] allergiesArray = foundMedicalRecord.getAllergies().toArray(new String[0]);
        personInfoDto.setAllergies(allergiesArray);
      } else {
        personInfoDto.setAllergies(new String[0]);  // Tableau vide
      }
    } else {
      // Valeurs par défaut si pas de dossier médical
      personInfoDto.setAge(0);    // ← CORRIGÉ : setAge(0) directement
      personInfoDto.setMedications(new String[0]);
      personInfoDto.setAllergies(new String[0]);
    }

    return personInfoDto;
  }

  // === MÉTHODES D'ÉCRITURE (CRÉATION, MISE À JOUR, SUPPRESSION) ===

  /**
   * Crée une nouvelle personne
   * @param person Personne à créer
   * @return Personne créée
   */
  public Person createPerson(Person person) {
    // Récupère la liste actuelle des personnes
    List<Person> persons = personRepository.getAllPersons();

    // Ajoute la nouvelle personne à la liste
    persons.add(person);

    // Log de confirmation
    System.out.println("✅ PERSONNE CRÉÉE : " + person.getFirstName() + " " + person.getLastName());

    return person;
  }

  /**
   * Met à jour une personne existante
   * @param updatedPerson Personne avec les nouvelles données
   * @return Personne mise à jour ou null si non trouvée
   */
  public Person updatePerson(Person updatedPerson) {
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

        // Log de confirmation
        System.out.println("✅ PERSONNE MISE À JOUR : " + updatedPerson.getFirstName() + " " + updatedPerson.getLastName());

        return updatedPerson;
      }
    }

    // Log d'erreur si personne non trouvée
    System.out.println("❌ PERSONNE NON TROUVÉE POUR MISE À JOUR : " + updatedPerson.getFirstName() + " " + updatedPerson.getLastName());

    return null;
  }

  /**
   * Supprime une personne
   * @param firstName Prénom de la personne à supprimer
   * @param lastName Nom de famille de la personne à supprimer
   * @return true si supprimée, false si non trouvée
   */
  public boolean deletePerson(String firstName, String lastName) {
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

        // Log de confirmation
        System.out.println("✅ PERSONNE SUPPRIMÉE : " + firstName + " " + lastName);

        return true;
      }
    }

    // Log d'erreur si personne non trouvée
    System.out.println("❌ PERSONNE NON TROUVÉE POUR SUPPRESSION : " + firstName + " " + lastName);

    return false;
  }

  /**
   * Récupère la liste de toutes les villes uniques
   * @return Liste des villes sans doublons
   */
  public List<String> getAllCities() {
    List<Person> persons = personRepository.getAllPersons();
    List<String> cities = new ArrayList<>();

    for (Person person : persons) {
      String city = person.getCity();
      if (city != null && !cities.contains(city)) {
        cities.add(city);  // Ajoute seulement si pas déjà présent
      }
    }

    System.out.println("🏙️ " + cities.size() + " villes trouvées: " + cities);
    return cities;
  }

  // === MÉTHODE UTILITAIRE PRIVÉE ===

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
}