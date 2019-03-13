package com.github.learningtour786.kafka.tutorial.classic;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread {

    public static final Logger LOG = LoggerFactory.getLogger(ConsumerDemoWithThread.class);

    public static void main(String[] args) {
        new ConsumerDemoWithThread().run();
    }

    private void run() {
        {
            LOG.info("ConsumerDemo Started...");

            //create a latch to deal multiple threads
            CountDownLatch latch = new CountDownLatch(1);

            //Create a consumer task
            String bootstrapServer = "localhost:9092";
            String groupId = "my-fifth-app";
            String topic = "firsttopic";
            Runnable consumerTask = new ConsumerTask(bootstrapServer, groupId, topic, latch);

            //start the task
            Thread consumerThread = new Thread(consumerTask);
            consumerThread.start();

            //add shutdown hook to stop this main thread
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOG.error("Got Shutdown hook...");
                try {
                    ((ConsumerTask) consumerTask).shutDownConsumer();
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    LOG.info("Application Exited...");
                }
            }));

            //wait for the task to finish
            try {
                latch.await();
            } catch (InterruptedException e) {
                LOG.error("Application interruped...");
                e.printStackTrace();
            }finally {
                LOG.info("Application Closing...");
            }
        }
    }

    public class ConsumerTask implements Runnable {

        private final Logger LOG = LoggerFactory.getLogger(ConsumerTask.class);

        private CountDownLatch latch;
        Properties properties;
        String bootstrapServer;
        String groupId;
        String topic;
        Consumer<String, String> consumer;

        public ConsumerTask(String bootstrapServer, String groupId, String topic, CountDownLatch latch) {
            this.bootstrapServer = bootstrapServer;
            this.groupId = groupId;
            this.topic = topic;

            properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                //creaate consumer
                consumer = new KafkaConsumer<String, String>(properties);

                //subscribe to only single topic
                //consumer.subscribe(Collections.singleton(TOPIC));

                //subscribe consumer to multiple topics
                consumer.subscribe(Arrays.asList(topic));

                //poll for data
                while (true) {
                    ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
                    consumerRecords.forEach(record->{
                        LOG.info("{}|{}|{}|{}|{}",record.key(),record.value(),record.topic(),record.partition(),record.offset());
                    });
                }
            }catch (WakeupException e) {
                LOG.error("Consumer Received shutdown singnal!");
                //e.printStackTrace();
            } finally {
                LOG.info("Closing the consumer...");
                consumer.close();
                latch.countDown();
            }
        }

        public void shutDownConsumer() {
            consumer.wakeup();//this will interrupt consumer.poll, hence it will throw WakeupException
        }
    }

}
