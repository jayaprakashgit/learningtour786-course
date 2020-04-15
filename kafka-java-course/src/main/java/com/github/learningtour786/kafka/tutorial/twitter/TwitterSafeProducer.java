package com.github.learningtour786.kafka.tutorial.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Refer the below url to know how to connect and stream data from tiwtter
 * https://github.com/twitter/hbc
 */
public class TwitterSafeProducer {

    public static final Logger logger = LoggerFactory.getLogger(TwitterSafeProducer.class);

    final String consumerKey;
    final String consumerSecret;
    final String token;
    final String tokenSecret;

    List<String> terms = Lists.newArrayList("narendra modi");

    Client client = null;
    Producer<String, String> kafkaProducer = null;

    private TwitterSafeProducer() {
        //https://github.com/twitter/hbc
        /*pass this variables from jvm options
        -DconsumerKey=<<value>>
        -DconsumerSecret=<<value>>
        -Dtoken=<<value>>
        -DtokenSecret=<<value>>*/
        consumerKey = System.getProperty("consumerKey");
        consumerSecret = System.getProperty("consumerSecret");
        token = System.getProperty("token");
        tokenSecret = System.getProperty("tokenSecret");
        if (StringUtils.isEmpty(consumerKey) || StringUtils.isEmpty(consumerSecret) || StringUtils.isEmpty(token) || StringUtils.isEmpty(tokenSecret)) {
            throw new RuntimeException("consumer and token secrets are required");
        }
    }

    public static void main(String[] args) {
        new TwitterSafeProducer().run();
    }

    private void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            logger.info("Application received stop request, hence stopping....");
            logger.info("stopping twitter client....");
            client.stop();
            logger.info("stopping kafka producer....");
            kafkaProducer.close();//It sends all the data in the memory before close the connection
            logger.info("done....");
        }));

        logger.info("Starting to run twitter producer...");
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        //BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        //create a twitter client
        client = createTwitterClient(msgQueue);
        // Attempts to establish a connection.
        client.connect();


        kafkaProducer = createKafkaProucer();

        logger.info("client successfully connected to twitter api...");
        // on a this or different thread, or multiple different threads....
        try {
            while (!client.isDone()) {
                String msg = null;
                try {
                    msg = msgQueue.poll(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (msg != null) {
                    logger.info("tweet => {}",msg);
                    kafkaProducer.send(new ProducerRecord<String, String>("twitter_topic", null, msg), new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if (e != null) {
                                logger.error("Something wrong.....",e);
                            }
                        }
                    });
                }
            }
        } finally {
            client.stop();
            logger.info("client connention stopped...");
        }
        logger.info("End of application...");
    }

    private Producer<String, String> createKafkaProucer() {
        Properties properties = new Properties();
        String bootstrapServers = "localhost:9092";
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create a safe producer props
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        //if kafka 2.0 >= 1.1, so we can keep this as 5, otherwise use 1, this will maintain order in case of retries
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        Producer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        return kafkaProducer;
    }

    private Client createTwitterClient(BlockingQueue<String> msgQueue) {
        logger.info("creating twitter client...");
        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        //List<Long> followings = Lists.newArrayList(1234L, 566788L);
        //hosebirdEndpoint.followings(followings);

        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));
        //.eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();

        return hosebirdClient;
    }
}
