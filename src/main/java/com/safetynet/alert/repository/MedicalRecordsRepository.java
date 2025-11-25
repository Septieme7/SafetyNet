package com.safetynet.alert.repository;

import com.safetynet.alert.model.Medicalrecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MedicalRecordsRepository {

  private final DataHandler dataHandler;

  public MedicalRecordsRepository (DataHandler dataHandler) {
    this.dataHandler = dataHandler;
  }

  public List<Medicalrecord> getAllMedicalrecords() {
    return dataHandler.getData().getMedicalrecords();
  }

}