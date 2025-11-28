package com.safetynet.alert.controller;


import com.safetynet.alert.model.FireStation;
import com.safetynet.alert.service.FireStationService;
import com.safetynet.alert.service.dto.FireDto;
import com.safetynet.alert.service.dto.FireStationDto;
import com.safetynet.alert.service.dto.FloodDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }



    //get all fireStation
    @GetMapping("allFirestation")
    public List<FireStation> allFireStation() {
        return this.fireStationService.allFireStations();
    }

    //get All number phone link with this station
    @GetMapping("phoneAlert")
    public List<String> findAllPhoneNumberByStation(@RequestParam(name = "firestation")String station) {
        return fireStationService.findAllPhoneNumberByStation(station);
    }

    //
    @GetMapping("firestation")
    public FireStationDto getPersonsByStation(@RequestParam("stationNumber") String stationNumber) {
        return fireStationService.getPersonsByStation(stationNumber);
    }

    @RequestMapping("flood/station")
    public List<FloodDto> flood(@RequestParam("stations")List<Integer> numbers) {
        return fireStationService.flood(numbers);
    }

    @GetMapping("fire")
    public List<FireDto> getFireDtoByAddress(@RequestParam("address")String address) {
        return fireStationService.getFireDtoByAddress(address);
    }


}
