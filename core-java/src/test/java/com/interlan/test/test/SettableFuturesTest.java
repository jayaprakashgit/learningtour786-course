package com.interlan.test.test;

import com.google.common.util.concurrent.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class SettableFuturesTest {

    private ExecutorService executorService;
    private CountDownLatch startSignal;
    private CountDownLatch endSignal;
    private static final int NUM_THREADS = 5;
    private boolean callbackRan;


    @BeforeEach
    public void setUp() {
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
        startSignal = new CountDownLatch(1);
        endSignal = new CountDownLatch(1);
        callbackRan = false;
    }

    @AfterEach
    public void tearDown() {
        executorService.shutdownNow();
    }

    //@Test
    public void testRunSettableFutureWithoutTimeout() throws Exception {
        SettableFuture<String> settableFuture = SettableFuture.create();

        Future<String> helloFuture = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " is executing...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello";
        });

        //settableFuture.(helloFuture);

        endSignal.await();
        Assertions.assertEquals(callbackRan, true);
    }

    @Test
    public void testRunListenableFutureWithFutureCallbackSuccess() throws Exception {
        Future<String> submit = executorService.submit(new Task(startSignal));
        ListenableFuture<String> futureTask = JdkFutureAdapters.listenInPoolThread(submit);
        FutureCallbackImpl callback = new FutureCallbackImpl();
        Futures.addCallback(futureTask, callback, executorService);
        //startSignal.countDown();
        //endSignal.await();
        int i = 3;
        while (!callback.isCompleted()) {
            System.out.println(Thread.currentThread().getName() +" is waiting to finish the runnable task");
            Thread.sleep(1000);
            i--;
        }
        Assertions.assertEquals(callback.getCallbackResult(), "Task Done successfully");
    }

    @Test
    public void testRunListenableFutureWithFutureCallbackFailure() throws Exception {
        Future<String> submit = executorService.submit(new Task(null));
        ListenableFuture<String> futureTask = JdkFutureAdapters.listenInPoolThread(submit);
        FutureCallbackImpl callback = new FutureCallbackImpl();
        Futures.addCallback(futureTask, callback, executorService);
        //startSignal.countDown();  don't call countdown
        endSignal.await();
        System.out.println(Thread.currentThread().getName() +" is waiting to finish the runnable task");
        Assertions.assertTrue(callback.getCallbackResult().contains("java.lang.NullPointerException"));
    }


    private class FutureCallbackImpl implements FutureCallback<String> {

        private volatile boolean completed;
        private StringBuilder builder = new StringBuilder();

        public boolean isCompleted(){
            return completed;
        }
        @Override
        public void onSuccess(String result) {
            builder.append(result).append(" successfully");
            done();
        }

        @Override
        public void onFailure(Throwable t) {
            builder.append(t.toString());
            done();
        }

        private void done() {
            System.out.println(Thread.currentThread().getName()+" is executing the callback");
            completed = true;
            endSignal.countDown();
        }

        public String getCallbackResult() {
            return builder.toString();
        }
    }


    private class Task implements Callable<String> {
        private CountDownLatch start;

        public Task() {
        }

        public Task(CountDownLatch start) {
            this.start = start;
        }

        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName() +" is executing the task");
            this.start.await(1, TimeUnit.SECONDS);
            Thread.sleep(1000);
            return "Task Done";
        }
    }


}
