package com.interlan.test.java7;

import java.util.ArrayList;
import java.util.List;

public class ListDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add(0, "3");
		list.add(1, "4");
		System.out.println(list);
	}

}
