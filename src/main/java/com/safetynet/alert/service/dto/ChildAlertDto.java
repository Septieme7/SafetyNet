package com.safetynet.alert.service.dto;

import java.util.List;

/**
 * DTO pour l'endpoint ChildAlert - Informations sur les enfants d'une adresse
 */
public class ChildAlertDto {
  private String firstName;
  private String lastName;
  private int age;
  private List<HouseholdMemberDto> householdMembers;

  public ChildAlertDto() {
  }

  public ChildAlertDto(String firstName, String lastName, int age, List<HouseholdMemberDto> householdMembers) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.householdMembers = householdMembers;
  }

  // Getters et Setters
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public int getAge() { return age; }
  public void setAge(int age) { this.age = age; }
  public List<HouseholdMemberDto> getHouseholdMembers() { return householdMembers; }
  public void setHouseholdMembers(List<HouseholdMemberDto> householdMembers) { this.householdMembers = householdMembers; }
}