package com.safetynet.alert.repository;


import com.safetynet.alert.model.MedicalRecord;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicalRecordRepository {

    private final DataHandler dataHandler;

    public MedicalRecordRepository(DataHandler dataHandler) {this.dataHandler = dataHandler;}

//    public List<MedicalRecord> findAllMedicalRecord() {
//        return dataHandler.getData().getMedicalrecords();
//    }

    public MedicalRecord findMedicalWithFirstNameAndLastName(String firstName,String lastName) {
        return dataHandler.getData().getMedicalrecords().stream()
                .filter(p->p.getFirstName().equals(firstName))
                .filter(p-> p.getLastName().equals(lastName))
                .findFirst()
                .orElseGet(() -> new MedicalRecord());
    }

    private boolean isUnder18(String birthdate) {
        Date date = null;
        try {
            date = new SimpleDateFormat("DD/MM/YYYY").parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR) - 18);
        return !calendar.getTime().after(date);
    }

    public List<MedicalRecord> findAllMedicalRecordsUnder18() {
        return dataHandler.getData().getMedicalrecords().stream()
                .filter(m -> isUnder18(m.getBirthdate()))
                .collect(Collectors.toList());
    }

}
