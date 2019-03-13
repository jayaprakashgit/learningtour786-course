package com.github.learningtour786.kafka.tutorial.classic;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.stream.IntStream;

public class ProducerDemoWithCallBack {

    public static final Logger LOG = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

    public static void main(String[] args) {
        System.out.println("ProducerDemo Started");

        Properties properties = new Properties();
        String bootstrapServers = "localhost:9092";
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        IntStream.rangeClosed(1, 10).forEach(value -> {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("firsttopic", "hello world : "+value);
            //send data in async
            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //this method execute every time a message is successfully sent or if any exception is thrown
                    LOG.info("onComplete Executed");
                    if (e != null) {
                        LOG.error("Exception Occured " + e.getMessage());
                    } else {
                        LOG.info("Successfully Processed Messge topic : {} | partition : {} | offset :  {} | timestamp : {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
                    }
                }
            });
        });

        producer.flush();
        producer.close();
    }
}
