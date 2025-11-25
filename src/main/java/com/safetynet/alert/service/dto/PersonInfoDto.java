package com.safetynet.alert.service.dto;

/**
 * DTO pour transporter les informations complètes d'une personne
 * Inclut les données personnelles, de contact et médicales
 */
public class PersonInfoDto {

  // === CHAMPS ===
  private String firstName;      // Prénom de la personne
  private String lastName;       // Nom de famille
  private String address;        // Adresse postale
  private String city;           // Ville
  private String zip;            // Code postal
  private String phone;          // Téléphone
  private int age;               // Âge calculé (en années)
  private String email;          // Adresse email
  private String[] medications;  // Liste des médicaments
  private String[] allergies;    // Liste des allergies

  // === CONSTRUCTEURS ===

  /**
   * Constructeur par défaut requis pour la désérialisation JSON
   */
  public PersonInfoDto() {
  }

  /**
   * Constructeur complet avec tous les champs
   */
  public PersonInfoDto(String firstName, String lastName, String address, String city, String zip, String phone, int age, String email, String[] medications, String[] allergies) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.city = city;
    this.zip = zip;
    this.phone = phone;
    this.age = age;
    this.email = email;
    this.medications = medications;
    this.allergies = allergies;
  }

  // === GETTERS ET SETTERS ===

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String[] getMedications() {
    return medications;
  }

  public void setMedications(String[] medications) {
    this.medications = medications;
  }

  public String[] getAllergies() {
    return allergies;
  }

  public void setAllergies(String[] allergies) {
    this.allergies = allergies;
  }
}