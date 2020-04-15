package com.github.learningtour786.kafka.tutorial.elasticsearch;

import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CouldElasticSearchAsyncConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CouldElasticSearchAsyncConsumer.class.getName());

    private static JsonParser jsonParser = new JsonParser();

    private final String elastic_hostname;
    private final String elastic_username;
    private final String elastic_password;

    private static String elasticSearchIndexName;
    private static String elasticSearchIndexType;

    public CouldElasticSearchAsyncConsumer() {
        // replace with your own credentials
        /*-Delastic_hostname=<<url>>
        -Delastic_username=<<username>>
        -Delastic_password=<<pwd>>*/
        elastic_hostname = System.getProperty("elastic_hostname"); // localhost or bonsai url
        elastic_username = System.getProperty("elastic_username"); // needed only for bonsai
        elastic_password = System.getProperty("elastic_password"); // needed only for bonsai
        if (StringUtils.isEmpty(elastic_hostname) || StringUtils.isEmpty(elastic_username) || StringUtils.isEmpty(elastic_password)) {
            throw new RuntimeException("elastic_hostname, elastic_username and elastic_password are required, supply thru system variabble");
        }
    }

    public static void main(String[] args) throws IOException {
        new CouldElasticSearchAsyncConsumer().run();
    }

    public void run() throws IOException {
        RestHighLevelClient client = createClient();
        KafkaConsumer<String, String> consumer = createConsumer("twitter_topic");

        BulkProcessor bulkProcessor = createAsyncBulkProcessor(client);

        while(true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // new in Kafka 2.0.0
            Integer recordCount = records.count();
            logger.info("Received " + recordCount + " records");

            //BulkRequest bulkRequest = new BulkRequest();

            for (ConsumerRecord<String, String> record : records){

                // 2 strategies
                // kafka generic ID
                // String id = record.topic() + "_" + record.partition() + "_" + record.offset();

                // twitter feed specific id
                try {
                    String id = extractIdFromTweet(record.value());

                    // where we insert data into ElasticSearch
                    elasticSearchIndexName = "twitter";
                    elasticSearchIndexType = "tweets";
                    IndexRequest indexRequest = new IndexRequest(elasticSearchIndexName, elasticSearchIndexType)
                            .id(id)
                            .source(XContentType.JSON, "real_time_tweets", record.value());

                    /*//this block is for indexing single document
                    IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                    String id1 = indexResponse.getId();
                    logger.info(id1);*/

                    //bulkRequest.add(indexRequest); // we add to our bulk request (takes no time)
                    bulkProcessor.add(indexRequest);
                } catch (NullPointerException e){
                    logger.warn("skipping bad data: " + record.value());
                }

            }

            if (recordCount > 0) {
                logger.info("Processing bulk operation...");
                //BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                bulkProcessor.flush();
                logger.info("Committing offsets...");
                consumer.commitSync();
                logger.info("Offsets have been committed");

                try {
                    boolean terminated = bulkProcessor.awaitClose(1000L, TimeUnit.MILLISECONDS);
                    logger.info("bulkProcessor terminated : {}",terminated);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // close the client gracefully
        // client.close();
    }

    private BulkProcessor createAsyncBulkProcessor(RestHighLevelClient client) {

        BulkProcessor.Listener listener = createBulkProcessorListener();

        BulkProcessor.Builder builder = BulkProcessor.builder(
                (request, bulkListener) ->
                        client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener);
/*
        builder.setBulkActions(500);
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        builder.setConcurrentRequests(0);
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
*/
        return builder.build();
    }

    private BulkProcessor.Listener createBulkProcessorListener() {
        return new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long executionId, BulkRequest request) {
                    int numberOfActions = request.numberOfActions();
                    logger.debug("Executing bulk [{}] with {} requests",
                            executionId, numberOfActions);
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request,
                                      BulkResponse response) {
                    if (response != null) {
                        response.iterator().forEachRemaining(bulkItemResponse -> {
                            logger.info("BulkItemResponse id :{}, Operation Type :{}",bulkItemResponse.getId(), bulkItemResponse.getOpType());
                        });
                    }
                    if (response.hasFailures()) {
                        logger.warn("Bulk [{}] executed with failures", executionId);
                    } else {
                        logger.debug("Bulk [{}] completed in {} milliseconds",
                                executionId, response.getTook().getMillis());
                    }
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request,
                                      Throwable failure) {
                    logger.error("Failed to execute bulk", failure);
                }
            };
    }

    public RestHighLevelClient createClient(){

        /////////// IF YOU USE LOCAL ELASTICSEARCH ///////////
        //  String elastic_hostname = "localhost";
        //  RestClientBuilder builder = RestClient.builder(new HttpHost(elastic_hostname,9200,"http"));

        /////////// IF YOU USE BONSAI / HOSTED ELASTICSEARCH ///////////
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elastic_username, elastic_password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(elastic_hostname, 443, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    public static KafkaConsumer<String, String> createConsumer(String topic){

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "kafka-demo-elasticsearch";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // disable auto commit of offsets
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); // poll only maximum 100 records per poll

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }

    private static String extractIdFromTweet(String tweetJson){
        // gson library
        return jsonParser.parse(tweetJson)
                .getAsJsonObject()
                .get("id_str")
                .getAsString();
    }
}
