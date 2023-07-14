package com.interlan.test.test;

import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

/**
 * http://codingjunkie.net/google-guava-concurrency-listenablefuture/
 */
public class ListenableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //normalFuturesSample();
        listenableFuturesSample();
    }

    private static void normalFuturesSample() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> helloFuture = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " is executing...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello";
        });
        System.out.println("helloFuture.get() = " + helloFuture.get());
        System.out.println("done");

        executorService.shutdown();
    }

    private static void listenableFuturesSample() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
        ListenableFuture<String> listenableFuture = listeningExecutorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " is executing...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello";
        });
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println("s = " + s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Exception happened "+throwable.getMessage());
            }
        }, Executors.newFixedThreadPool(1));

        executorService.shutdown();
        executorService.awaitTermination(10000, TimeUnit.SECONDS);
        System.out.println("done");
    }
}
