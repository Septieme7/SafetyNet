package com.safetynet.alert.repository;

import com.jsoniter.JsonIterator;
import com.safetynet.alert.model.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class DataHandler {

  private final Data data;

  public DataHandler() throws IOException {
    String temp = getFromRessource("data.json");
    this.data = JsonIterator.deserialize(temp, Data.class);
  }
  private String getFromRessource(String s) throws IOException, IOException {
    InputStream is = new ClassPathResource (s).getInputStream();
    return IOUtils.toString(is, StandardCharsets.UTF_8);
  }

  public Data getData() { return data; }
}