package com.safetynet.alert.model;

/**
 * Modèle représentant une personne dans le système
 */
public class Person {
  private String firstName;  // Prénom
  private String lastName;   // Nom de famille
  private String address;    // Adresse
  private String city;       // Ville
  private String zip;        // Code postal
  private String phone;      // Téléphone
  private String email;      // Email

  // Getters et setters pour chaque champ...
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }
  public String getZip() { return zip; }
  public void setZip(String zip) { this.zip = zip; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
}