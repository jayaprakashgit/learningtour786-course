package com.interlan.test.java7;

public class Test2 {

	static int total = 10;
	public void call(){
		int total =5;
		System.out.println(this.total);
	}
	
	public static void main(String arg[]){
		Test2 a = new Test2();
		a.call();
		a.display();
		
		if(1==2 | 2==2){
			System.out.println("true");
		}
	}
	
	private int data;
	public void display(){
		class Decrementer{
			public void dec(){
				data--;
			}
		}
		
		Decrementer d = new Decrementer();
		d.dec();
		System.out.println(data);
	}
}
