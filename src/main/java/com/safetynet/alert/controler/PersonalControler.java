package com.safetynet.alert.controler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class PersonControler{

  @RestController
  @RequestMapping("/community")
  public class CommunityController {




    @GetMapping("/email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String email )
    {
      return ResponseEntity.ok("Email reçu : " + email + " - ");
    }

  }
}