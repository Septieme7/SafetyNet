package com.safetynet.alert.service;

import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public List<String> findAllEmailsByCity(String city) {
    List<String> personsMails = new ArrayList<> ();
    List<Person> persons = personRepository.findAll ();
    for (Person person : persons) {
      if (person.getCity().equals(city)) {
        personsMails.add (person.getEmail());
      }
    }
    return personsMails;
  }

}