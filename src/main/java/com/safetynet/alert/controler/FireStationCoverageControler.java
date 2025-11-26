package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour la couverture des stations
 */
@RestController
public class FireStationCoverageControler {
  private final AlertService alertService;

  public FireStationCoverageControler(AlertService alertService) {
    this.alertService = alertService;
  }

  /**
   * Récupère la couverture d'une station
   */
  @GetMapping("/firestation")
  public Map<String, Object> getFireStationCoverage(@RequestParam String stationNumber) {
    System.out.println("🏠 COUVERTURE STATION: " + stationNumber);
    Map<String, Object> result = alertService.getFireStationCoverage(stationNumber);
    System.out.println("✅ COUVERTURE RÉCUPÉRÉE");
    return result;
  }
}
