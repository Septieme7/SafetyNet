package com.safetynet.alert.service;

import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.repository.MedicalRecordsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Service pour les opérations sur les dossiers médicaux
 */
@Service
public class MedicalRecordsService {
  private final MedicalRecordsRepository medicalRecordsRepository;

  public MedicalRecordsService(MedicalRecordsRepository medicalRecordsRepository) {
    this.medicalRecordsRepository = medicalRecordsRepository;
  }

  /**
   * Récupère tous les dossiers médicaux
   */
  public List<Medicalrecord> getAllMedicalRecords() {
    System.out.println("🏥 RÉCUPÉRATION TOUS LES DOSSIERS MÉDICAUX");
    return medicalRecordsRepository.getAllMedicalrecords();
  }

  /**
   * Ajoute un nouveau dossier médical
   */
  public Medicalrecord addMedicalRecord(Medicalrecord medicalRecord) {
    System.out.println("➕ AJOUT DOSSIER: " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
    List<Medicalrecord> medicalRecords = medicalRecordsRepository.getAllMedicalrecords();

    // Vérification de l'existence
    boolean exists = medicalRecords.stream()
            .anyMatch(mr -> mr.getFirstName().equals(medicalRecord.getFirstName())
                    && mr.getLastName().equals(medicalRecord.getLastName()));

    if (exists) {
      System.out.println("❌ DOSSIER EXISTANT");
      throw new RuntimeException("Dossier médical déjà existant");
    }

    medicalRecords.add(medicalRecord);
    System.out.println("✅ DOSSIER AJOUTÉ");
    return medicalRecord;
  }

  /**
   * Met à jour un dossier médical existant
   */
  public Medicalrecord updateMedicalRecord(Medicalrecord medicalRecord) {
    System.out.println("✏️  MISE À JOUR DOSSIER: " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
    List<Medicalrecord> medicalRecords = medicalRecordsRepository.getAllMedicalrecords();

    Optional<Medicalrecord> existingRecord = medicalRecords.stream()
            .filter(mr -> mr.getFirstName().equals(medicalRecord.getFirstName())
                    && mr.getLastName().equals(medicalRecord.getLastName()))
            .findFirst();

    if (existingRecord.isPresent()) {
      Medicalrecord recordToUpdate = existingRecord.get();
      recordToUpdate.setBirthdate(medicalRecord.getBirthdate());
      recordToUpdate.setMedications(medicalRecord.getMedications());
      recordToUpdate.setAllergies(medicalRecord.getAllergies());
      System.out.println("✅ DOSSIER MIS À JOUR");
      return recordToUpdate;
    }

    System.out.println("❌ DOSSIER NON TROUVÉ");
    throw new RuntimeException("Dossier médical non trouvé");
  }

  /**
   * Supprime un dossier médical
   */
  public void deleteMedicalRecord(String firstName, String lastName) {
    System.out.println("🗑️  SUPPRESSION DOSSIER: " + firstName + " " + lastName);
    List<Medicalrecord> medicalRecords = medicalRecordsRepository.getAllMedicalrecords();
    boolean removed = medicalRecords.removeIf(mr ->
            mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName));

    if (removed) {
      System.out.println("✅ DOSSIER SUPPRIMÉ");
    } else {
      System.out.println("❌ DOSSIER NON TROUVÉ");
      throw new RuntimeException("Dossier médical non trouvé");
    }
  }

  /**
   * Calcule l'âge à partir d'un dossier médical
   */
  public int calculateAgeFromMedicalRecord(String firstName, String lastName) {
    Medicalrecord medicalRecord = findMedicalRecordByName(firstName, lastName);
    if (medicalRecord != null && medicalRecord.getBirthdate() != null) {
      return calculateAge(medicalRecord.getBirthdate());
    }
    return 0;
  }

  /**
   * Trouve un dossier médical par nom
   */
  private Medicalrecord findMedicalRecordByName(String firstName, String lastName) {
    return medicalRecordsRepository.getAllMedicalrecords().stream()
            .filter(mr -> mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName))
            .findFirst()
            .orElse(null);
  }

  /**
   * Calcule l'âge à partir d'une date de naissance
   */
  private int calculateAge(String birthdate) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
      LocalDate birthDate = LocalDate.parse(birthdate, formatter);
      return Period.between(birthDate, LocalDate.now()).getYears();
    } catch (Exception e) {
      System.err.println("❌ ERREUR CALCUL ÂGE: " + birthdate);
      return 0;
    }
  }
}
