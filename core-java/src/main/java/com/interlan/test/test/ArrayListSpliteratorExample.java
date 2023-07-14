package com.interlan.test.test;

import com.interlan.util.CommonUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListSpliteratorExample {

    public List<Integer> multipleEachValue(List<Integer> integerList, boolean isParallel) {
        CommonUtil.startTimer();
        Stream<Integer> integerStream = integerList.stream();
        if (integerStream.isParallel()) {
            integerStream.parallel();
        }
        List<Integer> result = integerStream
                .map(item -> item * 2)
                .collect(Collectors.toList());
        CommonUtil.timeTaken();
        return result;
    }
}
