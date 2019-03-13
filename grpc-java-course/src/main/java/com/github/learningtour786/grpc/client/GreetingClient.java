package com.github.learningtour786.grpc.client;

import com.github.learningtour786.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello, I am gRPC Client");

        //String hostName = "localhost"; //This is to connect to localhost
        String hostName = "192.168.99.100"; //This is to connect to docker container host
        ManagedChannel channel = ManagedChannelBuilder.forAddress(hostName, 50051)
                .usePlaintext() //this will disable the ssl (by default ssl enabled) this shouldn't disable in prod
                .build();

        //Create sync stub
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
        //Create async stub
        //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

        //prepare the protocol greet message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Hello")
                .setLastName("World")
                .build();

        //Calling Unary API example rpc impelmentation
        //System.out.println("Calling doUnaryRpc........");
        //doUnaryRpc(channel, greeting);

        //Calling Server streaming example rpc implementation
        //System.out.println("Calling doServerStreamingRpc........");
        //doServerStreamingRpc(channel, greeting);


        //Calling Client streaming example rpc implementation
        System.out.println("Calling doClientStreamingRpc.......");
        doClientStreamingRpc(channel, greeting);

        //shutdown channel
        System.out.println("Shutting down the channel");
        channel.shutdown();
    }

    private static void doClientStreamingRpc(ManagedChannel channel, Greeting greeting) {
        //This is to block untill receive the final response from server
        CountDownLatch latch = new CountDownLatch(1);

        //Create asynch stub, otherwise we don't find the client streaming rpc method from stub
        GreetServiceGrpc.GreetServiceStub asyncClienttStub = GreetServiceGrpc.newStub(channel);

        //implementing streamobserver to handle what should i for each server response
        //basically what should we do when we get response from server
        StreamObserver<LongGreetRequest> requestObserver = asyncClienttStub.longGreet(new StreamObserver<LongGreetResponse>() {

            //we get response from server
            @Override
            public void onNext(LongGreetResponse value) {
                //onNext will be called only once as this is client streaming rpc
                System.out.println("Received onNext response from server");
                System.out.println(value.getResult());
            }

            //we get an error from server
            @Override
            public void onError(Throwable t) {

            }

            //when server is done, sending us data
            @Override
            public void onCompleted() {
                //onCompleted will be called right after onNext()
                System.out.println("Received onComplete response  from  server");
                latch.countDown();//This will release the block
            }
        });

        System.out.println("Sending message#1 to server");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Jaya")
                        .build())
                .build());

        System.out.println("Sending message#2 to server");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Prakash")
                        .build())
                .build());

        System.out.println("Sending message#3 to server");
        requestObserver.onNext(LongGreetRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Raj")
                        .build())
                .build());

        //We tell the server that the client is done sending  data
        //otherwise we don't receive response from server, because we have writen the server side response in server's onComplete method
        System.out.println("Calling stream completion event...");
        requestObserver.onCompleted();

        //This is to block 3ms or untill receive the final response from server
        try {
            System.out.println("Waiting for final response...");
            latch.await(3L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doServerStreamingRpc(ManagedChannel channel, Greeting greeting) {
        //Create blocking (synchronus) stub
        GreetServiceGrpc.GreetServiceBlockingStub greetServiceBlockingStub = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();
        Iterator<GreetManyTimesResponse> greetManyTimesResponseIterator = greetServiceBlockingStub.greetManyTimes(greetManyTimesRequest);
        //this iterator will get response as long as server send onCompleted (responseObserver.onCompleted();)
        greetManyTimesResponseIterator.forEachRemaining(greetManyTimesResponse -> {
            System.out.println(greetManyTimesResponse);
        });
    }

    private static void doUnaryRpc(ManagedChannel channel, Greeting greeting) {
        //Create blocking (synchronus) stub
        GreetServiceGrpc.GreetServiceBlockingStub greetServiceBlockingStub = GreetServiceGrpc.newBlockingStub(channel);

        //create a protocol buffer request message
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //do the rpc call (this is Unary rpc call, check this corresponding proto file to know whether its unary or streaming rpc)
        GreetResponse greetResponse = greetServiceBlockingStub.greet(greetRequest);

        //do desire thing with the rpc response
        String result = greetResponse.getResult();
        System.out.println("Received response from gRPC call");
        System.out.println(result);
    }
}
