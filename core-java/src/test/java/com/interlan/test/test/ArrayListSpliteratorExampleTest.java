package com.interlan.test.test;

import com.interlan.util.DataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ArrayListSpliteratorExampleTest {

    @BeforeAll
    static void init(){
        System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime().availableProcessors());
    }

    @RepeatedTest(5)
    void test_multipleEachValue_sequential(){
        ArrayListSpliteratorExample arrayListSpliteratorExample = new ArrayListSpliteratorExample();
        int maxNumber = 10_00_000;
        ArrayList<Integer> integerList = DataSet.generateArrayList(maxNumber);
        List<Integer> result = arrayListSpliteratorExample.multipleEachValue(integerList, false);
        Assertions.assertEquals(maxNumber, result.size());
    }

    @RepeatedTest(5)
    void test_multipleEachValue_parallel(){
        ArrayListSpliteratorExample arrayListSpliteratorExample = new ArrayListSpliteratorExample();
        int maxNumber = 10_00_000;
        ArrayList<Integer> integerList = DataSet.generateArrayList(maxNumber);
        List<Integer> result = arrayListSpliteratorExample.multipleEachValue(integerList, true);
        Assertions.assertEquals(maxNumber, result.size());
    }

}