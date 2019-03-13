package com.github.learningtour786.grpc.server;

import com.github.learningtour786.proto.greet.*;
import io.grpc.stub.StreamObserver;

import java.util.stream.IntStream;

public class GreetServerImpl extends GreetServiceGrpc.GreetServiceImplBase {

    //This is Unary API example implementation(aka Single Request and Single Response, no streaming)
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        //extract the input from request object
        Greeting greeting = request.getGreeting();

        //process the logic
        String greetMessage = "Welcome " + greeting.getFirstName() + " " + greeting.getLastName();

        //prepare the response object
        GreetResponse response = GreetResponse.newBuilder().setResult(greetMessage).build();

        //set the response to the observer
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();
    }

    //This is Server streaming example implementation
    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {

        Greeting greeting = request.getGreeting();

        try {
            IntStream.rangeClosed(1, 10).forEach(i -> {

                GreetManyTimesResponse greetManyTimesResponse = GreetManyTimesResponse.newBuilder()
                        .setResult("Hello " + greeting.getFirstName() + ", This is response number : " + i)
                        .build();

                responseObserver.onNext(greetManyTimesResponse);

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } finally {
            responseObserver.onCompleted();
        }
    }

    //This is Client streaming example implementation
    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {

        //This will tell how i am going to handle the stream of client request
        StreamObserver<LongGreetRequest> requestStreamObserver = new StreamObserver<LongGreetRequest>() {

            String result = "";

            //This is to handle each stream of request msg
            @Override
            public void onNext(LongGreetRequest value) {
                //Going to concatenate each request value
                System.out.println("Received message from client as "+value.getGreeting().getFirstName());
                result += "Hello " + value.getGreeting().getFirstName() + "! ";
            }

            //This is to handle if client sends error
            @Override
            public void onError(Throwable t) {

            }

            //This is to handle when  client is done, what should i do
            @Override
            public void onCompleted() {
                System.out.println("Sending final response as client is done");
                //as client done, setting the result to response observer object
                responseObserver.onNext(LongGreetResponse.newBuilder().setResult(result).build());
                //complete the response
                responseObserver.onCompleted();
            }
        };

        return requestStreamObserver;
    }
}
