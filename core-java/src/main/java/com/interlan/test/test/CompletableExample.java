package com.interlan.test.test;

import com.interlan.util.CommonUtil;

public class CompletableExample {

    public CompletableExample(){

    }

    public String getGreetingsFor(String input) {
        System.out.println(Thread.currentThread().getName() + " is taking 5 seconds to complete this complete getGreetingsFor method to complete");
        CommonUtil.delay(5000);
        return "Hello " + input;
    }
}
