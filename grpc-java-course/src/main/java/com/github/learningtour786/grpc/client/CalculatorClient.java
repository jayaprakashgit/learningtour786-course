package com.github.learningtour786.grpc.client;

import com.github.learningtour786.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {
    public static void main(String[] args) {
        //String hostName = "localhost"; //This is to connect to localhost
        String hostName = "192.168.99.100"; //This is to connect to docker container host
        ManagedChannel channel = ManagedChannelBuilder.forAddress(hostName, 50051)
                .usePlaintext()//to disable the ssl, should not disable in prod
                .build();

        /*doUnaryRpcCall(channel);
        doServerStreamingRpcCall(channel);
        doClientStreamingRpcCall(channel);*/
        doBiDirectionalStreamingRpcCall(channel);

    }

    private static void doBiDirectionalStreamingRpcCall(ManagedChannel channel) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncStub = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<MaxFindingResponse> responseStreamObserver = new StreamObserver<MaxFindingResponse>() {
            @Override
            public void onNext(MaxFindingResponse value) {
                System.out.println("Received Current MaxValue :"+value.getMaxValue());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server completed streaming...");
                latch.countDown();
            }
        };

        StreamObserver<MaxFindingRequest> requestStreamObserver = asyncStub.findMaximum(responseStreamObserver);

        Arrays.asList(1,5,3,6,2,20).forEach(value->{
            System.out.println("Sending value :"+value);
            requestStreamObserver.onNext(MaxFindingRequest.newBuilder().setInputNumber(value).build());

            //This is just for testing purpose to see the bidirectional streaming between client and server
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {
            latch.await(60L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void doClientStreamingRpcCall(ManagedChannel channel) {

        //Creating the asynchronous client stub to call client streaming api
        CalculatorServiceGrpc.CalculatorServiceStub asyncClientStub = CalculatorServiceGrpc.newStub(channel);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        //Create the responseObserver to provide to the server to use it
        StreamObserver<AvgCalculationResponse> responseStreamObserver = new StreamObserver<AvgCalculationResponse>() {
            @Override
            public void onNext(AvgCalculationResponse value) {
                //This will be called up on server call onNext its responseObserver
                System.out.println("Final Average Value is :"+value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                //This will  be ccalled up on server call its responseObserver onComplete
                System.out.println("Finished Streaming....");
                countDownLatch.countDown();
            }
        };

        //Call the api and get the requestObserver. By using this requestObserver client  can stream messages to server
        StreamObserver<AvgCalculationRequest> requestStreamObserver = asyncClientStub.average(responseStreamObserver);

        //client is streaming data to server one by one
        for (int i = 1; i <= 10; i = i + 3) {
            requestStreamObserver.onNext(AvgCalculationRequest.newBuilder().setNumber(i).build());
        }

        //initiate that the client stream done
        requestStreamObserver.onCompleted();

        try {
            countDownLatch.await(60L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doServerStreamingRpcCall(ManagedChannel channel) {
        //create the blocking (synchronous) stub
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorServiceBlockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        //example for server streaming rpc call
        long input_number = 37483924L;
        PrimeNumberDecompositionRequest primeNumberRequest = PrimeNumberDecompositionRequest.newBuilder().setNumber(input_number).build();
        Iterator<PrimeNumberDecompositionResponse> primeNumberResponseIterator = calculatorServiceBlockingStub.primeNumberDecomposition(primeNumberRequest);
        primeNumberResponseIterator.forEachRemaining(primeNumberResponse -> {
            System.out.print(primeNumberResponse.getPrimeFactor());
            System.out.print(",");
        });
    }

    private static void doUnaryRpcCall(ManagedChannel channel) {
        //create the blocking (synchronous) stub
        CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorServiceBlockingStub = CalculatorServiceGrpc.newBlockingStub(channel);

        //prepare the protobuffer input message
        Input input = Input.newBuilder()
                .setValue1(100)
                .setValue2(200)
                .build();

        //prepare the protobuffer request object
        CalculatorRequest calculatorRequest = CalculatorRequest.newBuilder()
                .setInput(input)
                .build();

        //example for Unary rpc call
        CalculatorResponse calculatorResponse = calculatorServiceBlockingStub.calculate(calculatorRequest);

        //print the calculator rpc response result
        int result = calculatorResponse.getResult();
        System.out.println("Sum Result : "+result);
    }
}
