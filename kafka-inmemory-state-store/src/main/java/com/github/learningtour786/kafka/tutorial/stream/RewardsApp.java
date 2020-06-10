package com.github.learningtour786.kafka.tutorial.stream;

import com.github.learningtour786.kafka.tutorial.stream.serde.AppSerdes;
import com.github.learningtour786.kafka.tutorial.stream.types.Notification;
import com.github.learningtour786.kafka.tutorial.stream.types.PosInvoice;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class RewardsApp {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, PosInvoice> KS0 = streamsBuilder.stream(AppConfigs.posTopicName, Consumed.with(AppSerdes.String(), AppSerdes.PosInvoice()));
        KStream<String, PosInvoice> KS1 = KS0.filter((storeId, posInvoice) -> posInvoice.getCustomerType().equalsIgnoreCase(AppConfigs.CUSTOMER_TYPE_PRIME));

        StoreBuilder<KeyValueStore<String, Double>> stateStoreBuilder = Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore(AppConfigs.REWARDS_STORE_NAME), AppSerdes.String(), AppSerdes.Double());
        streamsBuilder.addStateStore(stateStoreBuilder);

        //KStream<String, Notification> KS2 = KS1.transformValues(() -> new RewardsTransformer(), AppConfigs.REWARDS_STORE_NAME);

        //original pos topic partitioned with store-id, now we are repartitioning using temp topic with customer-id with the help of custom partitioner(RewardsPartitioner)
        KStream<String, PosInvoice> through = KS1.through(AppConfigs.REWARDS_TEMP_TOPIC,
                Produced.with(AppSerdes.String(), AppSerdes.PosInvoice(), new RewardsPartitioner()));

        //now transforming the posinvoice into notification. Also, during transformation we also do the calculation with store data
        KStream<String, Notification> transformedStream = through.transformValues(() -> new RewardsTransformer(), AppConfigs.REWARDS_STORE_NAME);

        //final transformed stream will be sink to the output destination topic
        transformedStream.to(AppConfigs.notificationTopic, Produced.with(AppSerdes.String(), AppSerdes.Notification()));

        logger.info("Starting Stream");
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            logger.info("Stopping Streams");
            kafkaStreams.cleanUp();
        }));

    }
}
