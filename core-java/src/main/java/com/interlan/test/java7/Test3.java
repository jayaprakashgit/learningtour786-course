package com.interlan.test.java7;

import java.util.ArrayList;

public class Test3 {

	public static void main(String[] args) {
		int i=0, j=0;
		int counter=0;
			outer1:
			for(i=0; i<4;i++){
				outre2:
				for(j=0;j<2;j++){
					counter++;
					if(i==2){
						i++;
					}
					continue outer1;
				}
			}
		System.out.println("i="+i+", counter="+counter);
		
		
		
		Object obj = new Test3();
		System.out.println(obj.toString());
		
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println(Runtime.getRuntime().freeMemory());
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
	
	

	
}
