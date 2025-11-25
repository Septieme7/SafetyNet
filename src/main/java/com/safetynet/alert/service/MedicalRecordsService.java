package com.safetynet.alert.service;

import com.safetynet.alert.model.Medicalrecord;
import com.safetynet.alert.repository.MedicalRecordsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordsService {

  private static final Logger logger = LoggerFactory.getLogger(MedicalRecordsService.class);

  private final MedicalRecordsRepository medicalRecordsRepository;

  public MedicalRecordsService(MedicalRecordsRepository medicalRecordsRepository) {
    this.medicalRecordsRepository = medicalRecordsRepository;
  }

  public List<Medicalrecord> getAllMedicalRecords() {
    logger.info("Récupération de tous les dossiers médicaux");
    return medicalRecordsRepository.getAllMedicalrecords();
  }

  public Medicalrecord addMedicalRecord(Medicalrecord medicalRecord) {
    logger.info("Ajout d'un nouveau dossier médical pour {} {}",
            medicalRecord.getFirstName(), medicalRecord.getLastName());

    List<Medicalrecord> medicalRecords = medicalRecordsRepository.getAllMedicalrecords();

    // Vérifier si le dossier existe déjà
    boolean exists = medicalRecords.stream()
            .anyMatch(mr -> mr.getFirstName().equals(medicalRecord.getFirstName())
                    && mr.getLastName().equals(medicalRecord.getLastName()));

    if (exists) {
      logger.warn("Dossier médical déjà existant pour {} {}",
              medicalRecord.getFirstName(), medicalRecord.getLastName());
      throw new RuntimeException("Dossier médical déjà existant");
    }

    medicalRecords.add(medicalRecord);
    logger.info("Dossier médical ajouté avec succès");
    return medicalRecord;
  }

  public Medicalrecord updateMedicalRecord(Medicalrecord medicalRecord) {
    logger.info("Mise à jour du dossier médical pour {} {}",
            medicalRecord.getFirstName(), medicalRecord.getLastName());

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

      logger.info("Dossier médical mis à jour avec succès");
      return recordToUpdate;
    }

    logger.warn("Dossier médical non trouvé pour {} {}",
            medicalRecord.getFirstName(), medicalRecord.getLastName());
    throw new RuntimeException("Dossier médical non trouvé");
  }

  public void deleteMedicalRecord(String firstName, String lastName) {
    logger.info("Suppression du dossier médical pour {} {}", firstName, lastName);

    List<Medicalrecord> medicalRecords = medicalRecordsRepository.getAllMedicalrecords();
    boolean removed = medicalRecords.removeIf(mr -> mr.getFirstName().equals(firstName)
            && mr.getLastName().equals(lastName));

    if (removed) {
      logger.info("Dossier médical supprimé avec succès");
    } else {
      logger.warn("Dossier médical non trouvé pour la suppression");
      throw new RuntimeException("Dossier médical non trouvé");
    }
  }

  public Medicalrecord findMedicalRecordByName(String firstName, String lastName) {
    logger.debug("Recherche du dossier médical pour {} {}", firstName, lastName);

    return medicalRecordsRepository.getAllMedicalrecords().stream()
            .filter(mr -> mr.getFirstName().equals(firstName)
                    && mr.getLastName().equals(lastName))
            .findFirst()
            .orElse(null);
  }

  public int calculateAgeFromMedicalRecord(String firstName, String lastName) {
    Medicalrecord medicalRecord = findMedicalRecordByName(firstName, lastName);
    if (medicalRecord != null && medicalRecord.getBirthdate() != null) {
      return calculateAge(medicalRecord.getBirthdate());
    }
    return 0;
  }

  private int calculateAge(String birthdate) {
    try {
      java.time.LocalDate birthDate = java.time.LocalDate.parse(birthdate,
              java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      return java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
    } catch (Exception e) {
      logger.error("Erreur lors du calcul de l'âge pour la date: {}", birthdate);
      return 0;
    }
  }
}