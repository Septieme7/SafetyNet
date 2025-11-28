package com.safetynet.alert.controller;


import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.PersonService;
import com.safetynet.alert.service.dto.ChildAlertDto;
import com.safetynet.alert.service.dto.PersonInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("person")
    public List<Person> findAllPerson() {
        return this.personService.findAllPerson();
    }

    @GetMapping("communityEmail")
    public List<String> findAllEmailsByCity(@RequestParam(name = "city") String city) {
        return personService.findAllEmailsByCity(city);
    }

    @GetMapping("personInfo")
    public List<PersonInfoDto> ListOfPersonWithMedicalRecords(@RequestParam String firstName, String lastName) {
        return personService.findAllPersonWithMedicalRecords(firstName, lastName);
    }

    @GetMapping("childAlert")
    public List<ChildAlertDto> ListOfChildUnder18ByAddress (@RequestParam (name = "address") String address) {
        return personService.findAllChildUnder18ByAddress(address);
    }

}




