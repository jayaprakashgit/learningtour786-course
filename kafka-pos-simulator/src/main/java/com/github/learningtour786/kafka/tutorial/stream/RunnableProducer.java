package com.github.learningtour786.kafka.tutorial.stream;

import com.github.learningtour786.kafka.tutorial.stream.datagenerator.InvoiceGenerator;
import com.github.learningtour786.kafka.tutorial.stream.types.PosInvoice;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class RunnableProducer implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private final AtomicBoolean stopper = new AtomicBoolean(false);
    private KafkaProducer<String, PosInvoice> producer;
    private String topicName;
    private InvoiceGenerator invoiceGenerator;
    private int produceSpeed;
    private int id;

    RunnableProducer(int id, KafkaProducer<String, PosInvoice> producer, String topicName, int produceSpeed) {
        this.id = id;
        this.producer = producer;
        this.topicName = topicName;
        this.produceSpeed = produceSpeed;
        this.invoiceGenerator = InvoiceGenerator.getInstance();
    }
    @Override
    public void run() {
        try {
            logger.info("Starting producer thread - " + id);
            while (!stopper.get()) {
                PosInvoice posInvoice = invoiceGenerator.getNextInvoice();
                producer.send(new ProducerRecord<>(topicName, posInvoice.getStoreID(), posInvoice), (recordMetadata, e) -> {
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
