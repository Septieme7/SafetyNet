package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import com.safetynet.alert.service.dto.FireDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FireControler {
  private final AlertService alertService;

  public FireControler(AlertService alertService) {
    this.alertService = alertService;
  }

  @GetMapping("/fire")
  public ResponseEntity<FireDto> getFireInfo(@RequestParam String address) {
    System.out.println("🔥 INFOS FEU ADRESSE: " + address);
    FireDto fireInfo = alertService.getFireInfo(address);
    System.out.println("✅ INFOS FEU RÉCUPÉRÉES");
    return ResponseEntity.ok(fireInfo);
  }
}