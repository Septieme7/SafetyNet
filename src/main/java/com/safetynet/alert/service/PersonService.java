package com.safetynet.alert.service;


import com.safetynet.alert.model.MedicalRecord;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.repository.MedicalRecordRepository;
import com.safetynet.alert.repository.PersonRepository;
import com.safetynet.alert.service.dto.ChildAlertDto;
import com.safetynet.alert.service.dto.PersonInfoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;


    public PersonService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<Person> findAllPerson(){
        return personRepository.findAllPerson();
    }

    //methode with stream for have a list of emails
    public List<String> findAllEmailsByCity(String city) {
        return this.personRepository.findAllPerson()
                .stream()
                .filter(p -> p.getCity().equals(city))
                .map(Person::getEmail).collect(Collectors.toList());
    }



/* Method with foreach
    //Convert a list of person into a list of string to get only email
    public List<String> findAllEmailsByCity(String city){
        List<String> emails = new ArrayList<>();
        List<Person> persons = personRepository.findAllPerson();
        for (Person person : persons){
            if (person.getCity().equals(city)){
                emails.add(person.getEmail());
            }
        }

        return emails;
    }
*/

    public int calculateAge(String birthDateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

     public List<PersonInfoDto> findAllPersonWithMedicalRecords(String firstName, String lastName) {
        List<PersonInfoDto> result = new ArrayList<>();
        PersonInfoDto dto = new PersonInfoDto();
        //get a list of persons by firstName and lastName
        Person person = personRepository.findPersonByFirstNameAnLastName(firstName, lastName);
        //get a list of medical records by firstName and lastName
        MedicalRecord medicalRecord = medicalRecordRepository.findMedicalWithFirstNameAndLastName(firstName, lastName);
        //pour chaque élément de personnes rechercher dans la liste des -18
        //je crée une troisième liste et je fait rentrer les noms qui correspondent
         dto.setLastName(person.getLastName());
         dto.setAddress(person.getAddress());
         dto.setAge(String.valueOf(calculateAge(medicalRecord.getBirthdate())));
         dto.setEmail(person.getEmail());
         dto.setAllergies(medicalRecord.getAllergies());
         dto.setMedications(medicalRecord.getMedications());
         result.add(dto);
         return result;
    }

    public List<ChildAlertDto> findAllChildUnder18ByAddress(String address) {
        List<ChildAlertDto> result = new ArrayList<>();
        //récuperer la liste des personnes habitants à cette adress
        List<Person> persons = personRepository.findAllPersonByAddress(address);
        //récuperer la liste des medical records de - de 18 ans
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAllMedicalRecordsUnder18();
        //pour chaque élément de personne rechercher dans la liste des -18ans
        // je crée une troisième liste et je fais rentrer les noms qui correspondent
        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordsContainsPerson(medicalRecords,person);
            if (medicalRecord != null) {
                ChildAlertDto dto = new ChildAlertDto();
                dto.setFirstName(person.getFirstName());
                dto.setLastName(person.getLastName());
                dto.setAge(String.valueOf(calculateAge(medicalRecord.getBirthdate())));
                dto.setHousehold(persons.stream().filter(p->!p.getFirstName().equals(person.getFirstName())).collect(Collectors.toList()));
                result.add(dto);
            }
        }
        return result;
    }

    //receptionne une personne et check si elle est dans la liste
    private MedicalRecord medicalRecordsContainsPerson(List<MedicalRecord> medicalRecords, Person person) {
        for (MedicalRecord medicalRecord : medicalRecords) {
            if (medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName())) {
                return  medicalRecord;
            }
        }
        return null;
    }
}
