package com.safetynet.alert.repository;


import com.safetynet.alert.model.FireStation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FireStationRepository {

    private final DataHandler dataHandler;

    public FireStationRepository(DataHandler dataHandler) { this.dataHandler = dataHandler;}

    public List<FireStation> findAllFireStations() {
        return dataHandler.getData().getFirestations();
    }

    public List<FireStation> findAllFireStationsByNumber(Integer number) {
        return dataHandler.getData().getFirestations().stream()
                .filter(f->f.getStation()
                        .equals(number.toString())).collect(Collectors.toList());
    }
//    public void save(FireStation fireStation) {
//         dataHandler.getData().getFirestations().add(fireStation);
//    }




}
