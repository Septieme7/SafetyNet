package com.safetynet.alert.model;

import java.util.List;

/**
 * Conteneur principal des données
 */
public class Data {
  private List<Person> persons;              // Liste des personnes
  private List<Firestation> firestations;    // Liste des casernes
  private List<Medicalrecord> medicalrecords; // Liste des dossiers médicaux

  public List<Person> getPersons() { return persons; }
  public void setPersons(List<Person> persons) { this.persons = persons; }

  public List<Firestation> getFirestations() { return firestations; }
  public void setFirestations(List<Firestation> firestations) { this.firestations = firestations; }

  public List<Medicalrecord> getMedicalrecords() { return medicalrecords; }
  public void setMedicalrecords(List<Medicalrecord> medicalrecords) { this.medicalrecords = medicalrecords; }
}
