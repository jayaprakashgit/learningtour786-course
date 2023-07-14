package com.interlan.test.test;

import com.google.common.util.concurrent.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListenableFuturesTest {

    private ListeningExecutorService executorService;
    private CountDownLatch startSignal;
    private CountDownLatch endSignal;
    private static final int NUM_THREADS = 5;
    private boolean callbackRan;


    @BeforeEach
    public void setUp() {
        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(NUM_THREADS));
        startSignal = new CountDownLatch(1);
        endSignal = new CountDownLatch(1);
        callbackRan = false;
    }

    @AfterEach
    public void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    public void testRunListenableFutureWithCallback() throws Exception {
        ListenableFuture<String> futureTask = executorService.submit(new Task());
        futureTask.addListener(new Runnable() {
            @Override
            public void run() {
                callbackRan = true;
                endSignal.countDown();
            }
        }, executorService);

        endSignal.await();
        Assertions.assertEquals(callbackRan, true);
    }

    @Test
    public void testRunListenableFutureWithFutureCallbackSuccess() throws Exception {
        ListenableFuture<String> futureTask = executorService.submit(new Task(startSignal));
        FutureCallbackImpl callback = new FutureCallbackImpl();
        Futures.addCallback(futureTask, callback, executorService);
        startSignal.countDown();
        endSignal.await();
        Assertions.assertEquals(callback.getCallbackResult(), "Task Done successfully");
    }

    @Test
    public void testRunListenableFutureWithFutureCallbackFailure() throws Exception {
        ListenableFuture<String> futureTask = executorService.submit(new Task(null));
        FutureCallbackImpl callback = new FutureCallbackImpl();
        Futures.addCallback(futureTask, callback, executorService);
        //startSignal.countDown();  don't call countdown
        endSignal.await();
        Assertions.assertTrue(callback.getCallbackResult().contains("java.lang.NullPointerException"));
    }


    private class FutureCallbackImpl implements FutureCallback<String> {

        private StringBuilder builder = new StringBuilder();

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
            this.start.await(1, TimeUnit.SECONDS);
            Thread.sleep(1000);
            return "Task Done";
        }
    }


}
