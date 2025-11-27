package com.safetynet.alert.service.dto;

/**
 * DTO pour les personnes dans la couverture d'une station
 */
public class FireStationPersonDto {
  private String firstName;
  private String lastName;
  private String address;
  private String phone;

  public FireStationPersonDto() {
  }

  public FireStationPersonDto(String firstName, String lastName, String address, String phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phone = phone;
  }

  // Getters et Setters
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
}