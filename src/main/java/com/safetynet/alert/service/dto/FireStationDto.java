package com.safetynet.alert.service.dto;

import java.util.List;

/**
 * DTO pour l'endpoint FireStation - Couverture d'une station
 */
public class FireStationDto {
  private String stationNumber;
  private long adultsCount;
  private long childrenCount;
  private List<FireStationPersonDto> persons;

  public FireStationDto() {
  }

  public FireStationDto(String stationNumber, long adultsCount, long childrenCount, List<FireStationPersonDto> persons) {
    this.stationNumber = stationNumber;
    this.adultsCount = adultsCount;
    this.childrenCount = childrenCount;
    this.persons = persons;
  }

  // Getters et Setters
  public String getStationNumber() { return stationNumber; }
  public void setStationNumber(String stationNumber) { this.stationNumber = stationNumber; }
  public long getAdultsCount() { return adultsCount; }
  public void setAdultsCount(long adultsCount) { this.adultsCount = adultsCount; }
  public long getChildrenCount() { return childrenCount; }
  public void setChildrenCount(long childrenCount) { this.childrenCount = childrenCount; }
  public List<FireStationPersonDto> getPersons() { return persons; }
  public void setPersons(List<FireStationPersonDto> persons) { this.persons = persons; }
}