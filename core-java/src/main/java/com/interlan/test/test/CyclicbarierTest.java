package com.interlan.test.test;

import java.util.Random;
import java.util.concurrent.*;

class Player implements Runnable{
    String name;
    int lapsToRun;
    CyclicBarrier barrier;
    Random random = new Random();
    Player(String name, int lapsToRun, CyclicBarrier barrier){
        this.name = name;
        this.lapsToRun = lapsToRun;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " ready to run");
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }


        int lapsCount = 0;
        long totalTime = 0;
        while (!Thread.currentThread().isInterrupted() && lapsCount < lapsToRun) {
            try {
                int millis = random.nextInt(2450, 2500);
                totalTime = totalTime + millis;
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                System.out.println(name + " stopped in race due to stop[interrupt] signal");
                Thread.currentThread().interrupt();
            }
            lapsCount++;
            System.out.println(name + " finished laps : "+lapsCount);
        }
        System.out.println(name + " finished all laps in " + totalTime + " millis");
    }
}
public class CyclicbarierTest {
    private static final long RACE_IN_SECONDS = 5;
    private static final int lapsToRun = 2;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            System.out.println("Race started, Race should finish with in "+ RACE_IN_SECONDS +" seconds");
        });

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Player("Jaya", lapsToRun, barrier));
        executorService.execute(new Player("Prakash", lapsToRun, barrier));
        executorService.shutdown();

        try {
            if (executorService.awaitTermination(RACE_IN_SECONDS, TimeUnit.SECONDS)) {
                executorService.shutdown();
                System.out.println("race finished with in " + RACE_IN_SECONDS + " seconds");
            }else {
                System.out.println("Timeout : All players, please stop running");
                executorService.shutdownNow();
            }
       } catch (InterruptedException e) {
            System.out.println("Something wrong, lets stop the race...");
            executorService.shutdownNow();
        }

        System.out.println("Done");
    }
}
