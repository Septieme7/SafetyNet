    package com.safetynet.alert.service;


    import com.safetynet.alert.model.FireStation;
    import com.safetynet.alert.model.MedicalRecord;
    import com.safetynet.alert.model.Person;
    import com.safetynet.alert.repository.DataHandler;
    import com.safetynet.alert.repository.FireStationRepository;
    import com.safetynet.alert.repository.MedicalRecordRepository;
    import com.safetynet.alert.repository.PersonRepository;
    import com.safetynet.alert.service.dto.FireDto;
    import com.safetynet.alert.service.dto.FireStationDto;
    import com.safetynet.alert.service.dto.FireStationPersonDto;
    import com.safetynet.alert.service.dto.FloodDto;
    import org.springframework.stereotype.Service;

    import java.time.LocalDate;
    import java.time.Period;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.atomic.AtomicInteger;
    import java.util.stream.Collectors;

    @Service
    public class FireStationService {

        private final FireStationRepository fireStationRepository;
        private final PersonRepository personRepository;
        private final MedicalRecordRepository medicalRecordRepository;
        private final PersonService personService;
        private final DataHandler dataHandler;

        public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository, PersonService personService, DataHandler dataHandler) {
            this.fireStationRepository = fireStationRepository;
            this.personRepository = personRepository;
            this.medicalRecordRepository = medicalRecordRepository;
            this.personService = personService;
            this.dataHandler = dataHandler;
        }

        public List<FireStation> allFireStations(){
            return fireStationRepository.findAllFireStations();
        }

        public List<String> findAllPhoneNumberByStation(String station) {
            List<String> adress =fireStationRepository.findAllFireStations().stream()
                    .filter(s -> s.getStation().equals(station))
                    .map(FireStation::getAddress)
                    .collect(Collectors.toList());

            return personRepository.findAllPerson().stream()
                    .filter(person -> adress.contains(person.getAddress()))
                    .map(Person::getPhone)
                    .collect(Collectors.toList());

            // Error before
    //         return this.fireStationRepository.findAllFireStations()
    //                .stream()
    //                .filter(s -> s.getStation().equals(station))
    //                 .map(FireStation::getAddress).collect(Collectors.toCollection()),
    //                 this.personRepository.findAllPerson().stream()
    //                         .filter(pe -> pe.getAddress().equals(station))
    //                         .map(Person::getAddress).collect(Collectors.toList())
    //                         //.stream().flatMap(personRepository.findAllPerson())
    //                         .stream().flatMap(FireStation -> f.getAdre)
    //        Stream.concat(
    //                fireStationRepository.findAllFireStations().stream(),
    //                personRepository.findAllPerson().stream()
    //                        .filter(station->)

        }

        public FireStationDto getPersonsByStation(String stationNumber) {

            List<String> coverAddresses = fireStationRepository.findAllFireStations().stream()
                    .filter(fs -> fs.getStation().equalsIgnoreCase(stationNumber))
                    .map(FireStation::getAddress)
                    .toList();

            List<Person> personsCovered = personRepository.findAllPerson().stream()
                    .filter(p -> coverAddresses.contains(p.getAddress()))
                    .toList();


            AtomicInteger adultsCount = new AtomicInteger();
            AtomicInteger childsCount = new AtomicInteger();

            List<FireStationPersonDto> personDtoList = personsCovered.stream()
                    .map(p -> {
                        MedicalRecord record = medicalRecordRepository.findMedicalWithFirstNameAndLastName(
                                p.getFirstName(),
                                p.getLastName()
                        );

                        int age = 0;
                        if (record != null) {
                            age = personService.calculateAge(record.getBirthdate());
                        }
                        //permit incrementer SET GET
                        if (age > 18) {
                            adultsCount.incrementAndGet();
                        } else {
                            childsCount.incrementAndGet();
                        }

                        return new FireStationPersonDto(
                                p.getFirstName(),
                                p.getLastName(),
                                p.getPhone(),
                                p.getAddress()
                        );
                    })
                    .toList();

            return new FireStationDto(
                    String.valueOf(childsCount.get()),
                    String.valueOf(adultsCount.get()),
                    personDtoList
            );
        }

       public List<FloodDto> flood(List<Integer> stationNumbers) {
            return stationNumbers.stream().flatMap(n ->fireStationRepository.findAllFireStationsByNumber(n)
                    .stream()).map(s -> FloodDto.builder()
                   .address(s.getAddress()).people(getPeopleByAddress(s.getAddress())).build()).collect(Collectors.toList());
       }

       public List<FloodDto.PersonDto> getPeopleByAddress(String address) {
            return personRepository.findAllPersonByAddress(address).stream().map(p -> mapToperson(p)).collect(Collectors.toList());
       }

        private FloodDto.PersonDto mapToperson(Person person) {
            MedicalRecord medicalRecord = medicalRecordRepository.findMedicalWithFirstNameAndLastName(person.getFirstName(), person.getLastName());
            return FloodDto.PersonDto.builder()
                    .lastName(person.getLastName())
                    .phone(person.getPhone())
                    .age(personService.calculateAge(medicalRecord.getBirthdate()))
                    .allergies(medicalRecord.getAllergies())
                    .medication(medicalRecord.getMedications())
                    .build();

       }

        public List<FireDto> getFireDtoByAddress(String address) {

            List<FireStation>fireStations=dataHandler.getData().getFirestations();
            List<MedicalRecord>medicalRecords=dataHandler.getData().getMedicalrecords();
            List<Person>persons=dataHandler.getData().getPersons();

            List<FireDto> fireDtos = new ArrayList<>();
            //Recupérer les adresses pour peupler le dto
            for (FireStation fs : fireStations){
                //on rentre dans la boucle, si cette adresse est égale à celle qui correspond
                if (fs.getAddress().equals(address)){
                    String stationNumber = fs.getStation();
                    //pareil pour les persons.
                    for (Person p : persons){
                        if (p.getAddress().equals(address)){
                            FireDto dto = new FireDto();
                            dto.setStation(stationNumber);
                            dto.setFirstName(p.getFirstName());
                            dto.setLastName(p.getLastName());
                            dto.setPhoneNumber(p.getPhone());
                            //ainsi que pour MR
                            for (MedicalRecord mr : medicalRecords) {
                                if (p.getFirstName().equals(mr.getFirstName()) && p.getLastName().equals(mr.getLastName())) {
                                    dto.setAge(String.valueOf(personService.calculateAge(mr.getBirthdate())));
                                    dto.setMedications(mr.getMedications());
                                    dto.setAllergies(mr.getAllergies());
                                }
                            }
                            //on ajoute dans le dto les données
                            fireDtos.add(dto);
                        }
                    }

                }
            }
        return fireDtos;
        }
    }
