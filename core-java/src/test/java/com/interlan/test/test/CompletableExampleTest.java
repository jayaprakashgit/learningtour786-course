package com.interlan.test.test;

import com.interlan.util.CommonUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CompletableExampleTest {

    CompletableExample completableExample = new CompletableExample();

    @Test
    void getGreetingsFor() {
        System.out.println("Started to get greetings");
        String result = completableExample.getGreetingsFor("JP");
        System.out.printf("Output : %s \n", result);
        System.out.println("Done");
    }

    @Test
    void getGreetingsForUsingCompletable() {
        System.out.println("Started to get greetings");
        CompletableFuture.supplyAsync(() -> completableExample.getGreetingsFor("JP"))
                //.thenApply(String::toUpperCase)
                //.thenApply(res -> res + "-" + res.length())
                .thenAccept(res -> System.out.println("res is : " + res));
        CommonUtil.delay(9000);
        System.out.println("Done");
    }

    @Test
    void getGreetingsForUsingCompletableCombineMethod() {
        CompletableFuture<String> completionStage1 = CompletableFuture.supplyAsync(() -> completableExample.getGreetingsFor("JP"));
        CompletableFuture<String> completionStage2 = CompletableFuture.supplyAsync(() -> {
            return "welcome to async programming";
        });
        completionStage1.thenCombine(completionStage2, (cs1output, cs2output) -> {
            return cs1output + " " + cs2output;
        }).thenAccept(finalResult -> System.out.println("finalResult = " + finalResult));
        CommonUtil.delay(9000);
        System.out.println("Done");
    }
}