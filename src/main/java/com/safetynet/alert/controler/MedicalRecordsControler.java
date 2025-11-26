package com.safetynet.alert.controler;

import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.service.MedicalRecordsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour les opérations sur les dossiers médicaux
 */
@RestController
public class MedicalRecordsControler {
  private final MedicalRecordsService medicalRecordsService;

  public MedicalRecordsControler(MedicalRecordsService medicalRecordsService) {
    this.medicalRecordsService = medicalRecordsService;
  }

  /**
   * Récupère tous les dossiers médicaux
   */
  @GetMapping("/medicalRecords")
  public List<Medicalrecord> getAllMedicalRecords() {
    System.out.println("🏥 TOUS LES DOSSIERS MÉDICAUX");
    return medicalRecordsService.getAllMedicalRecords();
  }

  /**
   * Ajoute un nouveau dossier médical
   */
  @PostMapping("/medicalRecord")
  public Medicalrecord addMedicalRecord(@RequestBody Medicalrecord medicalRecord) {
    System.out.println("➕ AJOUT DOSSIER: " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
    return medicalRecordsService.addMedicalRecord(medicalRecord);
  }

  /**
   * Met à jour un dossier médical
   */
  @PutMapping("/medicalRecord")
  public Medicalrecord updateMedicalRecord(@RequestBody Medicalrecord medicalRecord) {
    System.out.println("✏️  MISE À JOUR DOSSIER: " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
    return medicalRecordsService.updateMedicalRecord(medicalRecord);
  }

  /**
   * Supprime un dossier médical
   */
  @DeleteMapping("/medicalRecord")
  public void deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
    System.out.println("🗑️  SUPPRESSION DOSSIER: " + firstName + " " + lastName);
    medicalRecordsService.deleteMedicalRecord(firstName, lastName);
  }
}
