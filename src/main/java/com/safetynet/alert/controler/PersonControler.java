package com.safetynet.alert.controler;

import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.PersonService;
import com.safetynet.alert.service.dto.PersonInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer toutes les opérations sur les personnes
 */
@RestController
@RequestMapping("/person")
public class PersonControler {
  private final PersonService personService;

  public PersonControler(PersonService personService) {
    this.personService = personService;
  }

  /**
   * Récupère les informations complètes d'une personne
   */
  @GetMapping("/personInfo")
  public ResponseEntity<PersonInfoDto> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
    System.out.println("👤 RECHERCHE INFOS: " + firstName + " " + lastName);
    PersonInfoDto personInfo = personService.getPersonInfoDtoList(firstName, lastName);

    if (personInfo == null) {
      System.out.println("❌ PERSONNE NON TROUVÉE");
      return ResponseEntity.notFound().build();
    }

    System.out.println("✅ INFOS TROUVÉES");
    return ResponseEntity.ok(personInfo);
  }

  /**
   * Récupère tous les emails d'une ville
   */
  @GetMapping("/communityEmail")
  public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
    System.out.println("📧 EMAILS VILLE: " + city);
    List<String> emails = personService.findAllEmailsByCity(city);
    System.out.println("✅ " + emails.size() + " EMAIL(S) TROUVÉ(S)");
    return ResponseEntity.ok(emails);
  }

  /**
   * Crée une nouvelle personne
   */
  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody Person person) {
    System.out.println("➕ CRÉATION: " + person.getFirstName() + " " + person.getLastName());
    Person createdPerson = personService.createPerson(person);
    System.out.println("✅ PERSONNE CRÉÉE");
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
  }

  /**
   * Met à jour une personne existante
   */
  @PutMapping
  public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
    System.out.println("✏️  MISE À JOUR: " + person.getFirstName() + " " + person.getLastName());
    Person updatedPerson = personService.updatePerson(person);

    if (updatedPerson == null) {
      System.out.println("❌ PERSONNE NON TROUVÉE POUR MISE À JOUR");
      return ResponseEntity.notFound().build();
    }

    System.out.println("✅ PERSONNE MISE À JOUR");
    return ResponseEntity.ok(updatedPerson);
  }

  /**
   * Supprime une personne
   */
  @DeleteMapping
  public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
    System.out.println("🗑️  SUPPRESSION: " + firstName + " " + lastName);
    boolean isDeleted = personService.deletePerson(firstName, lastName);

    if (isDeleted) {
      System.out.println("✅ PERSONNE SUPPRIMÉE");
      return ResponseEntity.noContent().build();
    } else {
      System.out.println("❌ PERSONNE NON TROUVÉE POUR SUPPRESSION");
      return ResponseEntity.notFound().build();
    }
  }
}
