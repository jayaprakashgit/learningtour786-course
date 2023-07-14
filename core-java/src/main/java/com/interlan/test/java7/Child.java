package com.interlan.test.java7;

public class Child extends Parent{
	
	public Child(){
		count++;
	}
	
	public static void main(String arg[]){
		System.out.println("Count = "+getCount());
		Child obj = new Child();
		System.out.println("Count = "+getCount());
	}

	@Override
	protected int getLocalCode(String value, boolean isValidated){
		return 1;
	}
	
	//Concurrent
	
}


class Account<S>{
	private S accountType;
	public void add(S newType){ accountType = newType;}
	public S get(){return accountType;}
}

class Account1<T>{
	private T accountType;
	public void add(T newType){ accountType = newType;}
	public T get(){return accountType;}
}