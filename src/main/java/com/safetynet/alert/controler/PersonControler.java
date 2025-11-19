package com.safetynet.alert.controler;

import com.safetynet.alert.service.PersonService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PersonControler {

  private final PersonService personService;

  public PersonControler(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/communityEmail")
  public List<String> getEmailsByCity(@RequestParam String city) {
    return personService.findAllEmailsByCity(city);
  }
}
//GEt POWER
//  @RequestMapping(value = "CommunityEmail", method = RequestMethod.GET)
//  public List<String> ListeEmails(@RequestParam(name = "city") String city) {
//    return personControler.findAllEmailsByCity(city);
//  }




