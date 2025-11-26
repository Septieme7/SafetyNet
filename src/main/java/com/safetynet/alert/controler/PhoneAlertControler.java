package com.safetynet.alert.controler;

import com.safetynet.alert.service.FireStationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour les alertes téléphoniques
 */
@RestController
public class PhoneAlertControler {
  private final FireStationService fireStationService;

  public PhoneAlertControler(FireStationService fireStationService) {
    this.fireStationService = fireStationService;
  }

  /**
   * Récupère les numéros de téléphone par station
   */
  @GetMapping("/phoneAlert")
  public List<String> getPhoneAlert(@RequestParam String firestation) {
    System.out.println("📞 PHONE ALERT STATION: " + firestation);
    List<String> phones = fireStationService.findPhoneNumbersByStationsNumber(firestation);
    System.out.println("✅ " + phones.size() + " NUMÉRO(S) TROUVÉ(S)");
    return phones;
  }
}
