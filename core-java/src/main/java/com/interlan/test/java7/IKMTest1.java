package com.interlan.test.java7;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class IKMTest1 {

	private static int count;
	static {
		System.out.println("In block 1");
		count = 10;
	}
	
	private int[] data;
	{
		System.out.println("In block 2");
		data = new int[count];
		for(int i=0; i<count; i++){
			data[i]=i;
		}
	}
	public static void main(String args[]){ 
		/*import java.io.FileInputStream;
		import java.io.FileReader;
		import java.io.OutputStream;
		import java.io.PrintWriter;
		import java.io.Writer;*/
		
		System.out.println("Count = "+count);
		System.out.println("Before 1st call to new");
		IKMTest1 test1 = new IKMTest1();
		System.out.println("Before 2st call to new");
		IKMTest1 test2 = new IKMTest1();
	}
}
