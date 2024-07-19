package com.allica.customer;

import com.allica.customer.dto.Customer;
import com.allica.customer.entity.CustomerEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    public static String readJsonFile(String fileName)  {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName)));
        } catch (Exception exp) {
             throw new RuntimeException(exp);
        }
    }


    public static Customer getCustomer(String fileName)  {
       String jsonContent = readJsonFile(fileName);
       ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(jsonContent, Customer.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomerEntity getCustomerEntity(String fileName)  {
        String jsonContent = readJsonFile(fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(jsonContent, CustomerEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
