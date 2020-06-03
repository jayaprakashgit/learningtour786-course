package com.github.learningtour786.kafka.tutorial.stream;

import com.github.learningtour786.kafka.tutorial.stream.datagenerator.MarketPriceGenerator;
import com.github.learningtour786.kafka.tutorial.stream.types.MarketPrice;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class RunnableProducer implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private final AtomicBoolean stopper = new AtomicBoolean(false);
    private KafkaProducer<String, MarketPrice> producer;
    private String topicName;
    private MarketPriceGenerator marketPriceGenerator;
    private int produceSpeed;
    private int id;

    RunnableProducer(int id, KafkaProducer<String, MarketPrice> producer, String topicName, int produceSpeed) {
        this.id = id;
        this.producer = producer;
        this.topicName = topicName;
        this.produceSpeed = produceSpeed;
        this.marketPriceGenerator = MarketPriceGenerator.getInstance();
    }
    @Override
    public void run() {
        try {
            logger.info("Starting producer thread - " + id);
            while (!stopper.get()) {
                MarketPrice marketPrice = marketPriceGenerator.getNextMarketPrice();
                producer.send(new ProducerRecord<>(topicName, marketPrice.getSym(), marketPrice), (recordMetadata, e) -> {
                    if (e != null) {
                        logger.error("exception occoured {}", e.getMessage());
                    } else {
                        logger.info("recordMetadata = " + recordMetadata);
                    }
                });
                Thread.sleep(produceSpeed);
            }

        } catch (Exception e) {
            logger.error("Exception in Producer thread - " + id);
            throw new RuntimeException(e);
        }

    }

    void shutdown() {
        logger.info("Shutting down producer thread - " + id);
        stopper.set(true);

    }
}
