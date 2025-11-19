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
//Je veux
  public List<String> findAllEmailsByCity(String city) {
    //La liste de
    List<String> personsMails = new ArrayList<> ();
    List<Person> persons = personRepository.findAll ();
    //Boucle pour verif les persons qui sont dans la ville demander
    for (Person person : persons) {
      //Et alors SI la person ce trouve dans la ville alors rajout dans la liste de leurs EMAILS
      if (person.getCity().equals(city)) {
        personsMails.add(person.getEmail());
      }
    }
    //Voir le resultat
    return personsMails;
  }



}