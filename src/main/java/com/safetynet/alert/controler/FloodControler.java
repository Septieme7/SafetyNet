package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import com.safetynet.alert.service.dto.FloodPersonDto;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class FloodControler {
  private final AlertService alertService;

  public FloodControler (AlertService alertService) {
    this.alertService = alertService;
  }

  @GetMapping("/flood/stations")
  public Map<String, List<FloodPersonDto>> getFloodStations(@RequestParam String stations) {
    System.out.println("🌊 STATIONS FLOOD: " + stations);
    List<String> stationList = Arrays.asList(stations.split(","));
    Map<String, List<FloodPersonDto>> result = alertService.getFloodStations(stationList);
    System.out.println("✅ DONNÉES FLOOD RÉCUPÉRÉES");
    return result;
  }
}