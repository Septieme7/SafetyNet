package com.safetynet.alert.controler;

import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.PersonService;
import com.safetynet.alert.service.dto.ChildAlertDto;
import com.safetynet.alert.service.dto.PersonInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer toutes les opérations sur les personnes
 * Expose les endpoints CRUD (Create, Read, Update, Delete) et les requêtes métier
 */
@RestController
@RequestMapping("/person")
public class PersonControler {

  // === DÉPENDANCE SERVICE ===
  private final PersonService personService;

  /**
   * Constructeur avec injection de dépendance
   * Spring injecte automatiquement le PersonService
   */
  public PersonControler(PersonService personService) {
    this.personService = personService;
  }

  // === ENDPOINTS GET (LECTURE) ===

  /**
   * Récupère la liste complète de toutes les personnes
   * @return Liste de toutes les personnes avec statut 200
   */
  @GetMapping
  public ResponseEntity<List<Person>> getAllPersons() {
    System.out.println("👥 RÉCUPÉRATION DE TOUTES LES PERSONNES");

    List<Person> persons = personService.getAllPersons();

    System.out.println("✅ " + persons.size() + " PERSONNE(S) TROUVÉE(S)");
    return ResponseEntity.ok(persons);
  }

  /**
   * Récupère les informations complètes d'une personne spécifique
   * @param firstName Prénom de la personne (requis)
   * @param lastName Nom de famille de la personne (requis)
   * @return PersonInfoDto avec toutes les infos ou 404 si non trouvé
   */
  @GetMapping("/personInfo")
  public ResponseEntity<PersonInfoDto> getPersonInfo(
          @RequestParam String firstName,   // Paramètre requis : firstName
          @RequestParam String lastName) {  // Paramètre requis : lastName

    System.out.println("👤 RECHERCHE INFOS PERSONNE : " + firstName + " " + lastName);

    // Appel du service pour récupérer les informations
    PersonInfoDto personInfo = personService.getPersonInfoDtoList(firstName, lastName);

    // Vérifie si la personne a été trouvée
    if (personInfo == null) {
      System.out.println("❌ PERSONNE NON TROUVÉE : " + firstName + " " + lastName);
      return ResponseEntity.notFound().build();  // HTTP 404 - Non trouvé
    }

    System.out.println("✅ INFOS PERSONNE TROUVÉES : " + firstName + " " + lastName);
    return ResponseEntity.ok(personInfo);  // HTTP 200 - OK avec les données
  }

  /**
   * Récupère tous les emails des personnes d'une ville
   * @param city Ville pour filtrer les emails
   * @return Liste des emails ou liste vide
   */
  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getEmailsByCity(
          @RequestParam String city) {  // Paramètre requis : city

    System.out.println("📧 RECHERCHE EMAILS POUR LA VILLE : " + city);

    // Appel du service pour récupérer les emails
    List<String> emails = personService.findAllEmailsByCity(city);

    // Log du résultat
    System.out.println("✅ " + emails.size() + " email(s) trouvé(s) pour la ville : " + city);
    if (!emails.isEmpty()) {
      System.out.println("📋 Liste des emails : " + emails);
    } else {
      System.out.println("❌ Aucun email trouvé pour cette ville");
    }

    return ResponseEntity.ok(emails);  // HTTP 200 avec la liste des emails
  }

  /**
   * Récupère les enfants habitant à une adresse avec les membres du foyer
   * @param address Adresse pour la recherche d'enfants
   * @return ChildAlertDto avec les enfants et membres du foyer ou 404 si aucun enfant
   */
  @GetMapping("/childAlert")
  public ResponseEntity<ChildAlertDto> getChildAlert(@RequestParam String address) {
    System.out.println("👶 RECHERCHE ENFANTS POUR L'ADRESSE : " + address);

    // Appel du service pour récupérer les alertes enfants
    ChildAlertDto childAlert = personService.getChildAlert(address);

    // Vérifie si des enfants ont été trouvés
    if (childAlert == null) {
      System.out.println("❌ AUCUN ENFANT TROUVÉ À L'ADRESSE : " + address);
      return ResponseEntity.notFound().build();  // HTTP 404 - Non trouvé
    }

    System.out.println("✅ ALERTE ENFANTS TROUVÉE POUR : " + address);
    return ResponseEntity.ok(childAlert);  // HTTP 200 - OK avec les données
  }

  /**
   * Récupère la liste de toutes les villes uniques
   * @return Liste des villes sans doublons
   */
  @GetMapping("/cities")
  public ResponseEntity<List<String>> getAllCities() {
    System.out.println("🏙️ RÉCUPÉRATION DE TOUTES LES VILLES");

    // Appel du service pour récupérer les villes
    List<String> cities = personService.getAllCities();

    System.out.println("✅ " + cities.size() + " VILLE(S) TROUVÉE(S) : " + cities);
    return ResponseEntity.ok(cities);  // HTTP 200 avec la liste des villes
  }

  // === ENDPOINT POST (CRÉATION) ===

  /**
   * Crée une nouvelle personne dans le système
   * @param person Objet Person envoyé dans le body JSON
   * @return Personne créée avec statut 201
   */
  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody Person person) {
    System.out.println("➕ CRÉATION NOUVELLE PERSONNE : " + person.getFirstName() + " " + person.getLastName());

    // Appel du service pour créer la personne
    Person createdPerson = personService.createPerson(person);

    System.out.println("✅ PERSONNE CRÉÉE AVEC SUCCÈS : " + person.getFirstName() + " " + person.getLastName());

    // Retourne HTTP 201 (Created) avec la personne créée
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
  }

  // === ENDPOINT PUT (MISE À JOUR) ===

  /**
   * Met à jour une personne existante
   * @param person Personne avec les nouvelles données
   * @return Personne mise à jour ou 404 si non trouvée
   */
  @PutMapping
  public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
    System.out.println("✏️  MISE À JOUR PERSONNE : " + person.getFirstName() + " " + person.getLastName());

    // Appel du service pour mettre à jour
    Person updatedPerson = personService.updatePerson(person);

    // Vérifie si la personne a été trouvée et mise à jour
    if (updatedPerson == null) {
      System.out.println("❌ PERSONNE NON TROUVÉE POUR MISE À JOUR : " + person.getFirstName() + " " + person.getLastName());
      return ResponseEntity.notFound().build();  // HTTP 404 - Non trouvé
    }

    System.out.println("✅ PERSONNE MISE À JOUR AVEC SUCCÈS : " + person.getFirstName() + " " + person.getLastName());
    return ResponseEntity.ok(updatedPerson);  // HTTP 200 - OK avec données mises à jour
  }

  // === ENDPOINT DELETE (SUPPRESSION) ===

  /**
   * Supprime une personne du système
   * @param firstName Prénom de la personne à supprimer
   * @param lastName Nom de famille de la personne à supprimer
   * @return 204 si supprimé, 404 si non trouvé
   */
  @DeleteMapping
  public ResponseEntity<Void> deletePerson(
          @RequestParam String firstName,   // Paramètre requis : firstName
          @RequestParam String lastName) {  // Paramètre requis : lastName

    System.out.println("🗑️  SUPPRESSION PERSONNE : " + firstName + " " + lastName);

    // Appel du service pour supprimer
    boolean isDeleted = personService.deletePerson(firstName, lastName);

    // Vérifie si la suppression a réussi
    if (isDeleted) {
      System.out.println("✅ PERSONNE SUPPRIMÉE AVEC SUCCÈS : " + firstName + " " + lastName);
      return ResponseEntity.noContent().build();  // HTTP 204 - No Content (succès)
    } else {
      System.out.println("❌ PERSONNE NON TROUVÉE POUR SUPPRESSION : " + firstName + " " + lastName);
      return ResponseEntity.notFound().build();   // HTTP 404 - Non trouvé
    }
  }
}