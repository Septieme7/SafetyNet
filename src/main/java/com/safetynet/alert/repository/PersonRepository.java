package com.safetynet.alert.repository;


import com.safetynet.alert.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonRepository {

    private final DataHandler dataHandler;

    public PersonRepository(DataHandler dataHandler) { this.dataHandler = dataHandler;}

    public List<Person> findAllPerson() {
        return dataHandler.getData().getPersons();
    }

    public Person findPersonByFirstNameAnLastName(String firstName, String lastName){
        return dataHandler.getData().getPersons().stream()
                .filter(p->p.getFirstName().equals(firstName))
                .filter(p->p.getLastName().equals(lastName))
                .findFirst()
                .orElseGet(()->new Person());
    }

    public List<Person> findAllPersonByAddress(String address) {
        return  dataHandler.getData().getPersons()
                .stream().filter(p->p.getAddress().equals(address)).collect(Collectors.toList());
    }

}
