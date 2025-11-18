package com.safetynet.Repository;

import com.safetynet.alert.model.Data;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.jsoniter.output.JsonStream;
import org.apache.commons.io.IOUtils;
import com.jsoniter.JsonIterator;

@Component
public class DataHandler {

  private final Data data;

  public DataHandler() throws IOException {
    String temp = getFromResource("data.json");
    this.data = JsonIterator.deserialize(temp, Data.class);
  }

  private String getFromResource(String s) throws IOException {
    InputStream is = new ClassPathResource(s).getInputStream ();
    return IOUtils.toString(is, StandardCharsets.UTF_8);
  }

  public Data getData() { return data; }
  }


