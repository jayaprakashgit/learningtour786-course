package com.github.learningtour786.kafka.tutorial.classic;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        System.out.println("ProducerDemo Started");

        Properties properties = new Properties();
        String bootstrapServers = "localhost:9092";
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("firsttopic", "hello world");

        //send data in async
        producer.send(producerRecord);
        producer.flush();
        producer.close();
    }
}
