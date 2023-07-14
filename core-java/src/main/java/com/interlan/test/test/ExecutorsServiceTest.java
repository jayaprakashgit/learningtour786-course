package com.interlan.test.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorsServiceTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(()-> {
            try {
                System.out.println("Starting thread : " + Thread.currentThread().getName());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("something interrupted");
                Thread.currentThread().interrupt();
                //throw new RuntimeException(e);
            }
        });

        executorService.execute(()-> {
            System.out.println("Starting thread : " + Thread.currentThread().getName());
        });

        try {
            if(executorService.awaitTermination(3000, TimeUnit.MILLISECONDS)){
                System.out.println("executor terminated");
                executorService.shutdown();
            }else {
                System.out.println("executor timeout happened");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("shutting down executor service normally");
        executorService.shutdown();
        System.out.println("All Done");
    }
}
