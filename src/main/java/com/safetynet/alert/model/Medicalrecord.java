package com.safetynet.alert.model;

import java.util.List;

/**
 * Modèle représentant un dossier médical
 */
public class Medicalrecord {
  private String firstName;          // Prénom
  private String lastName;           // Nom de famille
  private String birthdate;          // Date de naissance (format: "MM/dd/yyyy")
  private List<String> medications;  // Liste des médicaments
  private List<String> allergies;    // Liste des allergies

  // Getters et setters...
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getBirthdate() { return birthdate; }
  public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

  public List<String> getMedications() { return medications; }
  public void setMedications(List<String> medications) { this.medications = medications; }

  public List<String> getAllergies() { return allergies; }
  public void setAllergies(List<String> allergies) { this.allergies = allergies; }
}