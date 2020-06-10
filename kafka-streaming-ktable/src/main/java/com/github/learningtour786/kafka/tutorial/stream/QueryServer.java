package com.github.learningtour786.kafka.tutorial.stream;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Spark;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.List;

class QueryServer {
    private static final Logger logger = LogManager.getLogger();
    private final String NO_RESULTS = "No Results Found";
    private final String APPLICATION_NOT_ACTIVE = "Application is not active. Try later.";
    private final KafkaStreams streams;
    private Boolean isActive = false;
    private final HostInfo hostInfo;
    private Client client;

    QueryServer(KafkaStreams streams, String hostname, int port) {
        this.streams = streams;
        this.hostInfo = new HostInfo(hostname, port);
        client = ClientBuilder.newClient();
    }

    void setActive(Boolean state) {
        isActive = state;
    }

    private List<KeyValue<String, String>> readAllFromLocal() {

        List<KeyValue<String, String>> localResults = new ArrayList<>();
        ReadOnlyKeyValueStore<String, String> stateStore =
            streams.store(
                AppConfigs.stateStoreName,
                QueryableStoreTypes.keyValueStore()
            );

        stateStore.all().forEachRemaining(localResults::add);
        return localResults;
    }

    void start() {
        String kt01_store_end_point = "/" + AppConfigs.stateStoreName + "/all";

        logger.info("Starting Query Server at http://" + hostInfo.host() + ":" + hostInfo.port()
            + kt01_store_end_point);

        Spark.port(hostInfo.port());

        Spark.get(kt01_store_end_point, (req, res) -> {

            List<KeyValue<String, String>> allResults;
            String results;

            if (!isActive) {
                results = APPLICATION_NOT_ACTIVE;
            } else {
                allResults = readAllFromLocal();
                results = (allResults.size() == 0) ? NO_RESULTS
                    : allResults.toString();
            }
            return results;
        });

    }

    void stop() {
        client.close();
        Spark.stop();
    }

}
