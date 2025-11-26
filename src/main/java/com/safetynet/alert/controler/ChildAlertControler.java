package com.safetynet.alert.controler;

import com.safetynet.alert.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur pour les alertes enfants
 */
@RestController
public class ChildAlertControler {
  private final AlertService alertService;

  public ChildAlertControler(AlertService alertService) {
    this.alertService = alertService;
  }

  /**
   * Récupère les enfants à une adresse
   */
  @GetMapping("/childAlert")
  public Map<String, Object> getChildAlert(@RequestParam String address) {
    System.out.println("👶 ALERTE ENFANTS ADRESSE: " + address);
    Map<String, Object> fireInfo = alertService.getFireInfo(address);

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> persons = (List<Map<String, Object>>) fireInfo.get("persons");

    // Filtrer les enfants (<= 18 ans)
    List<Map<String, Object>> children = persons.stream()
            .filter(person -> {
              Integer age = (Integer) person.get("age");
              return age != null && age <= 18;
            })
            .collect(Collectors.toList());

    // Filtrer les adultes (> 18 ans)
    List<Map<String, Object>> adults = persons.stream()
            .filter(person -> {
              Integer age = (Integer) person.get("age");
              return age != null && age > 18;
            })
            .collect(Collectors.toList());

    Map<String, Object> result = Map.of(
            "children", children,
            "adults", adults
    );

    System.out.println("✅ " + children.size() + " ENFANT(S) TROUVÉ(S)");
    return result;
  }
}
