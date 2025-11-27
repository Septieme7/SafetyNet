package com.safetynet.alert.service.dto;

import java.util.List;

/**
 * DTO pour les personnes dans l'endpoint Fire
 */
public class FirePersonDto {
  private String firstName;
  private String lastName;
  private String phone;
  private int age;
  private List<String> medications;
  private List<String> allergies;

  public FirePersonDto() {
  }

  public FirePersonDto(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.age = age;
    this.medications = medications;
    this.allergies = allergies;
  }

  // Getters et Setters
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }
  public List<String> getMedications() { return medications; }
  public void setMedications(List<String> medications) { this.medications = medications; }
  public List<String> getAllergies() { return allergies; }
  public void setAllergies(List<String> allergies) { this.allergies = allergies; }
}