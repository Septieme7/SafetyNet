package com.safetynet.alert.controler;

import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.service.MedicalRecordsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordsControler {

  private final MedicalRecordsService medicalRecordsService;

  public MedicalRecordsControler(MedicalRecordsService medicalRecordsService) {
    this.medicalRecordsService = medicalRecordsService;
  }

  // GET tous les dossiers médicaux
  @GetMapping("/medicalRecords")
  public List<Medicalrecord> getAllMedicalRecords() {
    return medicalRecordsService.getAllMedicalRecords();
  }

  // POST - Ajouter un dossier médical
  @PostMapping("/medicalRecord")
  public Medicalrecord addMedicalRecord(@RequestBody Medicalrecord medicalRecord) {
    return medicalRecordsService.addMedicalRecord(medicalRecord);
  }

  // PUT - Mettre à jour un dossier médical
  @PutMapping("/medicalRecord")
  public Medicalrecord updateMedicalRecord(@RequestBody Medicalrecord medicalRecord) {
    return medicalRecordsService.updateMedicalRecord(medicalRecord);
  }

  // DELETE - Supprimer un dossier médical
  @DeleteMapping("/medicalRecord")
  public void deleteMedicalRecord(@RequestParam String firstName,
                                  @RequestParam String lastName) {
    medicalRecordsService.deleteMedicalRecord(firstName, lastName);
  }
}