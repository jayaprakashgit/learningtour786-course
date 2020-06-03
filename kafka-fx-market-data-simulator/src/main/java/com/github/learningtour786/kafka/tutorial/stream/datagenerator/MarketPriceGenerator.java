package com.github.learningtour786.kafka.tutorial.stream.datagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.learningtour786.kafka.tutorial.stream.types.MarketPrice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.util.Random;

public class MarketPriceGenerator {
    private static final Logger logger = LogManager.getLogger();
    private static MarketPriceGenerator ourInstance = new MarketPriceGenerator();
    private final Random msgId;
    private final Random marketPriceIndex;
    private final MarketPrice[] marketPrices;


    public static MarketPriceGenerator getInstance() {
        return ourInstance;
    }

    private MarketPriceGenerator() {
        String DATAFILE = "src/main/resources/data/MarketPrice.json";
        ObjectMapper mapper;
        marketPriceIndex = new Random();
        msgId = new Random();
        mapper = new ObjectMapper();
        try {
            marketPrices = mapper.readValue(new InputStreamReader(getClass().getResourceAsStream("/data/MarketPrice.json")), MarketPrice[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getIndex() {
        return marketPriceIndex.nextInt(10);
    }

    private int getNewMsgId() {
        return msgId.nextInt(99999999) + 99999;
    }

    public MarketPrice getNextMarketPrice() {
        MarketPrice marketPrice = marketPrices[getIndex()];
        marketPrice.setMsgId(Integer.toString(getNewMsgId()));
        marketPrice.setCreatedTime(System.currentTimeMillis());
/*
        marketPrice.setSym("SGDUSD");
        marketPrice.setBrokerID("REUTERS");
        marketPrice.setPublisherID("SPOT/REUTERS");
        marketPrice.setProductType("DEL");
        marketPrice.setUpdateType("INTIAL_IMAGE");
        marketPrice.setSupportStatus(2);
        marketPrice.setBid(1.4);
        marketPrice.setAsk(1.6);
        marketPrice.setMid(1.5);
*/
        logger.debug(marketPrice);
        return marketPrice;
    }
}
