package com.safetynet.alert.model;

/**
 * Modèle représentant une caserne de pompiers
 */
public class Firestation {
  private String address;  // Adresse couverte
  private String station;  // Numéro de station

  public Firestation() {}

  public Firestation(String address, String station) {
    this.address = address;
    this.station = station;
  }

  // GET et SET
  public String getAddress() { return address;}
  public void setAddress(String address) { this.address = address; }

  public String getStation() { return station; }
  public void setStation(String station) { this.station = station; }
}
