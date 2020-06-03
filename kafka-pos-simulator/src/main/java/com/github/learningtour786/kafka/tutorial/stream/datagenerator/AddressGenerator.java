package com.github.learningtour786.kafka.tutorial.stream.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.learningtour786.kafka.tutorial.stream.types.DeliveryAddress;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Random;

class AddressGenerator {
    private static final AddressGenerator ourInstance = new AddressGenerator();
    private final Random random;

    private DeliveryAddress[] addresses;

    private int getIndex() {
        return random.nextInt(100);
    }

    static AddressGenerator getInstance() {
        return ourInstance;
    }

    private AddressGenerator() {
        final String DATAFILE = "src/main/resources/data/address.json";
        final ObjectMapper mapper;
        random = new Random();
        mapper = new ObjectMapper();
        try {
            //addresses = mapper.readValue(new File(DATAFILE), DeliveryAddress[].class);
            addresses = mapper.readValue(new InputStreamReader(getClass().getResourceAsStream("/data/address.json")), DeliveryAddress[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    DeliveryAddress getNextAddress() {
        return addresses[getIndex()];
    }

}
