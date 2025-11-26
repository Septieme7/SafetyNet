package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour les alertes d'inondation
 */
@RestController
public class FloodControler {
  private final AlertService alertService;

  public FloodControler(AlertService alertService) {
    this.alertService = alertService;
  }

  /**
   * Récupère les informations pour plusieurs stations (inondation)
   */
  @GetMapping("/flood/stations")
  public Map<String, Object> getFloodStations(@RequestParam String stations) {
    System.out.println("🌊 STATIONS FLOOD: " + stations);
    List<String> stationList = Arrays.asList(stations.split(","));
    Map<String, Object> result = alertService.getFloodStations(stationList);
    System.out.println("✅ DONNÉES FLOOD RÉCUPÉRÉES");
    return result;
  }
}
