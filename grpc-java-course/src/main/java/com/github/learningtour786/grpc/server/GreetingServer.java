package com.github.learningtour786.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello gRPC");
        System.out.println("Starting GreetingServer.");

        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServerImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( ()->{
            System.out.println("Received Shutdown request");
            server.shutdown();
            System.out.println("Successfullly stopped the server");
        } ));

        System.out.println("GreetingServer Running...");
        server.awaitTermination();
    }
}
