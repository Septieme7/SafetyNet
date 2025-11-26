package com.safetynet.alert.repository;

import com.safetynet.alert.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Repository pour l'accès aux personnes
 */
@Component
public class PersonRepository {
  private final DataHandler dataHandler;

  public PersonRepository(DataHandler dataHandler) {
    this.dataHandler = dataHandler;
  }

  public List<Person> getAllPersons() {
    return dataHandler.getData().getPersons();
  }
}
