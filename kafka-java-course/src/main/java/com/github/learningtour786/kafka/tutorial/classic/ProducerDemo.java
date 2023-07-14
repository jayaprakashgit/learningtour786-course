package com.github.learningtour786.kafka.tutorial.classic;

import com.github.learningtour786.kafka.tutorial.config.AppConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.*;
import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        System.out.println("ProducerDemo Started");
        ProducerDemo producerDemo = new ProducerDemo();
        producerDemo.run();
    }

    public void run() {
        Properties properties = init();

/*
            Properties properties = new Properties();
            String bootstrapServers = "localhost:9092";
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
*/
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("firsttopic", "hello world");

        //send data in async
        producer.send(producerRecord);
        producer.flush();
        producer.close();

    }

    public Properties init() throws RuntimeException {
        Properties props = new Properties();
        try (Reader in = new InputStreamReader(new FileInputStream(AppConfigs.kafkaConfigFileLocation))) {
            //InputStream inputStream = new FileInputStream(AppConfigs.kafkaConfigFileLocation);
            props.load(in);

            props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load properties", e);
        }
        return props;
    }
}
