package com.safetynet.alert.service.dto;

import java.util.List;

/**
 * DTO pour l'endpoint Fire - Informations des personnes à une adresse avec la station
 */
public class FireDto {
  private String stationNumber;
  private List<FirePersonDto> persons;

  public FireDto() {
  }

  public FireDto(String stationNumber, List<FirePersonDto> persons) {
    this.stationNumber = stationNumber;
    this.persons = persons;
  }

  // Getters et Setters
  public String getStationNumber() { return stationNumber; }
  public void setStationNumber(String stationNumber) { this.stationNumber = stationNumber; }
  public List<FirePersonDto> getPersons() { return persons; }
  public void setPersons(List<FirePersonDto> persons) { this.persons = persons; }
}