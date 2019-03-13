package com.github.learningtour786.kafka.tutorial.classic;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoGroups {

    public static final Logger LOG = LoggerFactory.getLogger(ConsumerDemoGroups.class);

    public static void main(String[] args) {
        LOG.info("ConsumerDemo Started...");

        String bootstrapServer = "localhost:9092";
        String groupId = "my-fifth-app";
        String TOPIC = "firsttopic";

        //create server properties
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //creaate consumer
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //subscribe to only single topic
        //consumer.subscribe(Collections.singleton(TOPIC));

        //subscribe consumer to multiple topics
        consumer.subscribe(Arrays.asList(TOPIC));

        //poll for data
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            consumerRecords.forEach(record->{
                LOG.info("{}|{}|{}|{}|{}",record.key(),record.value(),record.topic(),record.partition(),record.offset());
            });

        }
    }
}
