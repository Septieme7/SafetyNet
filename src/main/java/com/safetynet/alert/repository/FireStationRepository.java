package com.safetynet.alert.repository;

import com.safetynet.alert.model.Firestation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Repository pour l'accès aux casernes
 */
@Component
public class FireStationRepository {
  private final DataHandler dataHandler;

  public FireStationRepository(DataHandler dataHandler) {
    this.dataHandler = dataHandler;
  }

  public List<Firestation> findAllFireStations() {
    return dataHandler.getData().getFirestations();
  }
}
