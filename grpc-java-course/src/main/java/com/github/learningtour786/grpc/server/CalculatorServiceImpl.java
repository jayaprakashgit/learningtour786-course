package com.github.learningtour786.grpc.server;

import com.github.learningtour786.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void calculate(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
        //parse the request object
        Input input = request.getInput();

        //sum the given input values
        int result = input.getValue1() + input.getValue2();

        //prepare the response object
        CalculatorResponse response = CalculatorResponse.newBuilder()
                .setResult(result)
                .build();

        //set the response object to the responseObserver
        responseObserver.onNext(response);

        //finish the rpc call
        responseObserver.onCompleted();
    }

    //This is an example for server streaming rpc implementation
    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
        long n = request.getNumber();
        int k = 2;

        while (n > 1) {
            if (n % k == 0) {
                PrimeNumberDecompositionResponse primeNumberResponse = PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(k)
                        .build();
                responseObserver.onNext(primeNumberResponse);
                n = n / k;
            } else {
                k = k + 1;
            }
        }

        responseObserver.onCompleted();
    }

    //This is an example for client streaming rpc implementation
    @Override
    public StreamObserver<AvgCalculationRequest> average(StreamObserver<AvgCalculationResponse> responseObserver) {

        StreamObserver<AvgCalculationRequest> requestStreamObserver = new StreamObserver<AvgCalculationRequest>() {
            float sum = 0;
            int msgCount = 0;
            @Override
            public void onNext(AvgCalculationRequest value) {
                sum += value.getNumber();
                msgCount += 1;
                System.out.println("Received Input Number : " + value.getNumber());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                Float result = new Float(sum / msgCount);
                System.out.println("sendig result : "+result);
                responseObserver.onNext(AvgCalculationResponse.newBuilder().setResult(result).build());
                responseObserver.onCompleted();
            }
        };

        return requestStreamObserver;
    }

    //This is an example for BiDi streaming rpc implementation
    @Override
    public StreamObserver<MaxFindingRequest> findMaximum(StreamObserver<MaxFindingResponse> responseObserver) {
        StreamObserver<MaxFindingRequest> requestStreamObserver = new StreamObserver<MaxFindingRequest>() {

            float currentMax = 0;

            @Override
            public void onNext(MaxFindingRequest value) {
                if (value.getInputNumber() > currentMax) {
                    currentMax = value.getInputNumber();
                    responseObserver.onNext(MaxFindingResponse.newBuilder().setMaxValue(currentMax).build());
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                //send the current maximum
                responseObserver.onNext(MaxFindingResponse.newBuilder().setMaxValue(currentMax).build());

                //the server is done sending data
                responseObserver.onCompleted();
            }
        };

        return requestStreamObserver;
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        int inputNumber = request.getInputNumber();
        if (inputNumber >= 0) {
            double sqrt = Math.sqrt(inputNumber);
            responseObserver.onNext(SquareRootResponse.newBuilder()
                    .setResult(sqrt)
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid argument supplied")
                    .augmentDescription("Input Number is : "+inputNumber)
                    .asRuntimeException()
            );
        }
    }
}
