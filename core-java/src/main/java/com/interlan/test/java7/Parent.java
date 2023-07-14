package com.interlan.test.java7;

public class Parent {

	protected static int count = 0;
	
	public Parent(){
		count++;
	}
	static int getCount(){
		return count;
	}
	
	protected int getLocalCode(String value, boolean isValidated){
		return 1;
	}
}
