package com.github.learningtour786.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting gRPC : CalculatorServer");
        Server server = ServerBuilder.forPort(50051)
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Received Shutdown request");
            server.shutdown();
            System.out.println("Shutdown completed");
        }));

        System.out.println("Running gRPC : CalculatorServer");
        server.awaitTermination();
    }
}
