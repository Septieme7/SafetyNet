package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import com.safetynet.alert.service.dto.FireStationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FireStationCoverageControler {
  private final AlertService alertService;

  public FireStationCoverageControler(AlertService alertService) {
    this.alertService = alertService;
  }

  @GetMapping("/firestation")
  public ResponseEntity<FireStationDto> getFireStationCoverage(@RequestParam String stationNumber) {
    System.out.println("🏠 COUVERTURE STATION: " + stationNumber);
    FireStationDto coverage = alertService.getFireStationCoverage(stationNumber);
    System.out.println("✅ COUVERTURE RÉCUPÉRÉE");
    return ResponseEntity.ok(coverage);
  }
}