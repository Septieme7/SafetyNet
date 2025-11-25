package com.safetynet.alert.controler;

// === IMPORTS ===
import com.safetynet.alert.model.Person;                   // Modèle Person
import com.safetynet.alert.service.PersonService;          // Service Person
import com.safetynet.alert.service.dto.PersonInfoDto;      // DTO PersonInfo
import org.springframework.http.HttpStatus;                 // Codes HTTP
import org.springframework.http.ResponseEntity;            // Réponse HTTP
import org.springframework.web.bind.annotation.*;          // Annotations REST

import java.util.List;                                     // Interface List

/**
 * Contrôleur REST pour gérer toutes les opérations sur les personnes
 * Expose les endpoints CRUD (Create, Read, Update, Delete)
 */
@RestController  // Déclare cette classe comme contrôleur REST
@RequestMapping("/person")  // Préfixe URL pour tous les endpoints
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
   * Récupère les informations complètes d'une personne spécifique
   * @param firstName Prénom de la personne (requis)
   * @param lastName Nom de famille de la personne (requis)
   * @return PersonInfoDto avec toutes les infos ou 404 si non trouvé
   */
  @GetMapping("/personInfo")  // GET /person/personInfo?firstName=X&lastName=Y
  public ResponseEntity<PersonInfoDto> getPersonInfo(
          @RequestParam String firstName,   // Paramètre requis : firstName
          @RequestParam String lastName) {  // Paramètre requis : lastName

    // Appel du service pour récupérer les informations
    PersonInfoDto personInfo = personService.getPersonInfoDtoList(firstName, lastName);

    // Vérifie si la personne a été trouvée
    if (personInfo == null) {
      return ResponseEntity.notFound().build();  // HTTP 404 - Non trouvé
    }

    return ResponseEntity.ok(personInfo);  // HTTP 200 - OK avec les données
  }

  /**
   * Récupère tous les emails des personnes d'une ville
   * @param city Ville pour filtrer les emails
   * @return Liste des emails ou liste vide
   */
  @GetMapping("/communityEmail")  // GET /person/communityEmail?city=X
  public ResponseEntity<List<String>> getEmailsByCity(
          @RequestParam String city) {  // Paramètre requis : city

    // Appel du service pour récupérer les emails
    List<String> emails = personService.findAllEmailsByCity(city);

    return ResponseEntity.ok(emails);  // HTTP 200 avec la liste des emails
  }

  // === ENDPOINT POST (CRÉATION) ===

  /**
   * Crée une nouvelle personne dans le système
   * @param person Objet Person envoyé dans le body JSON
   * @return Personne créée avec statut 201
   */
  @PostMapping  // POST /person
  public ResponseEntity<Person> createPerson(@RequestBody Person person) {
    // Appel du service pour créer la personne
    Person createdPerson = personService.createPerson(person);

    // Retourne HTTP 201 (Created) avec la personne créée
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
  }

  // === ENDPOINT PUT (MISE À JOUR) ===

  /**
   * Met à jour une personne existante
   * @param person Personne avec les nouvelles données
   * @return Personne mise à jour ou 404 si non trouvée
   */
  @PutMapping  // PUT /person
  public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
    // Appel du service pour mettre à jour
    Person updatedPerson = personService.updatePerson(person);

    // Vérifie si la personne a été trouvée et mise à jour
    if (updatedPerson == null) {
      return ResponseEntity.notFound().build();  // HTTP 404 - Non trouvé
    }

    return ResponseEntity.ok(updatedPerson);  // HTTP 200 - OK avec données mises à jour
  }

  // === ENDPOINT DELETE (SUPPRESSION) ===

  /**
   * Supprime une personne du système
   * @param firstName Prénom de la personne à supprimer
   * @param lastName Nom de famille de la personne à supprimer
   * @return 204 si supprimé, 404 si non trouvé
   */
  @DeleteMapping  // DELETE /person?firstName=X&lastName=Y
  public ResponseEntity<Void> deletePerson(
          @RequestParam String firstName,   // Paramètre requis : firstName
          @RequestParam String lastName) {  // Paramètre requis : lastName

    // Appel du service pour supprimer
    boolean isDeleted = personService.deletePerson(firstName, lastName);

    // Vérifie si la suppression a réussi
    if (isDeleted) {
      return ResponseEntity.noContent().build();  // HTTP 204 - No Content (succès)
    } else {
      return ResponseEntity.notFound().build();   // HTTP 404 - Non trouvé
    }
  }
}