package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour les informations d'incendie
 */
@RestController
public class FireControler {
  private final AlertService alertService;

  public FireControler(AlertService alertService) {
    this.alertService = alertService;
  }

  /**
   * Récupère les informations pour un incendie à une adresse
   */
  @GetMapping("/fire")
  public Map<String, Object> getFireInfo(@RequestParam String address) {
    System.out.println("🔥 INFOS FEU ADRESSE: " + address);
    Map<String, Object> result = alertService.getFireInfo(address);
    System.out.println("✅ INFOS FEU RÉCUPÉRÉES");
    return result;
  }
}
