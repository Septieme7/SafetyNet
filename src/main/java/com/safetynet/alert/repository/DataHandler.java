package com.safetynet.alert.repository;


import com.safetynet.alert.model.Data;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class DataHandler {

    private Data data;

    public DataHandler() throws IOException {
        String temp = getFromResource("data.json");
        this.data = JsonIterator.deserialize(temp,Data.class);
    }
    private String getFromResource(String s) throws IOException {
        InputStream is = new ClassPathResource(s).getInputStream();
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public Data getData() { return data;}

}
