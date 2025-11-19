package com.safetynet.alert.repository;

import com.safetynet.alert.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonRepository {

  private final DataHandler dataHandler;

  public PersonRepository(DataHandler dataHandler) {
    this.dataHandler = dataHandler;
  }

  public List<Person> findAll() {
    return dataHandler.getData().getPersons();
  }

}