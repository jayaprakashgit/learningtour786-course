package com.github.learningtour786.kafka.tutorial.stream;

import com.github.learningtour786.kafka.tutorial.stream.types.PosInvoice;
import org.apache.kafka.streams.processor.StreamPartitioner;

public class RewardsPartitioner implements StreamPartitioner<String, PosInvoice> {
    @Override
    public Integer partition(String topic, String key, PosInvoice posInvoice, int numOfPartitions) {
        return posInvoice.getCashierID().hashCode() % numOfPartitions;
    }
}
