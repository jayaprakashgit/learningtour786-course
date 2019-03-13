package com.github.learningtour786.kafka.tutorial.classic;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoAssignSeekForReplay {

    public static final Logger LOG = LoggerFactory.getLogger(ConsumerDemoAssignSeekForReplay.class);

    public static void main(String[] args) {
        LOG.info("ConsumerDemo Started...");

        String bootstrapServer = "localhost:9092";
        String topic = "firsttopic";

        //create server properties
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //creaate consumer
        Consumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //subscribe to only single topic
        //consumer.subscribe(Collections.singleton(TOPIC));

        //assign and seek are mostly used to replay data or fetch a spcific message
        TopicPartition topicPartition = new TopicPartition(topic, 0);
        //assign
        consumer.assign(Arrays.asList(topicPartition));

        //seek
        long offsetToReadFrom = 15L;
        consumer.seek(topicPartition, offsetToReadFrom);

        int numOfMsgToRead = 3;
        boolean keepOnReading = true;
        int numOfMsgReadSoFar = 0;

        //poll for data
        while (keepOnReading) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : consumerRecords) {
                numOfMsgReadSoFar++;
                LOG.info("{}|{}|{}|{}|{}",record.key(),record.value(),record.topic(),record.partition(),record.offset());
                if (numOfMsgReadSoFar > numOfMsgToRead) {
                    keepOnReading = false;
                    break;
                }
            }
        }

        LOG.info("Exiting the Application");
    }
}
