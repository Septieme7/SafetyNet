package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import com.safetynet.alert.service.dto.ChildAlertDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChildAlertControler {
  private final AlertService alertService;

  public ChildAlertControler(AlertService alertService) {
    this.alertService = alertService;
  }

  @GetMapping("/childAlert")
  public ResponseEntity<ChildAlertDto> getChildAlert(@RequestParam String address) {
    System.out.println("👶 ALERTE ENFANTS ADRESSE: " + address);
    ChildAlertDto childAlert = alertService.getChildAlert(address);

    if (childAlert == null) {
      System.out.println("❌ AUCUN ENFANT TROUVÉ À CETTE ADRESSE");
      return ResponseEntity.notFound().build();
    }

    System.out.println("✅ ALERTE ENFANTS RÉCUPÉRÉE");
    return ResponseEntity.ok(childAlert);
  }
}