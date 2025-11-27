package com.safetynet.alert.service.dto;

/**
 * DTO pour les membres du foyer dans ChildAlert
 */
public class HouseholdMemberDto {
  private String firstName;
  private String lastName;

  public HouseholdMemberDto() {
  }

  public HouseholdMemberDto(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  // Getters et Setters
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
}