package com.interlan.test.test;

import java.util.Arrays;
import java.util.Scanner;

class MyOwnQueue{

	int initialSize;
	Object[] data;
	int size = 0;
	
	public MyOwnQueue(int initialSize) {
		// TODO Auto-generated constructor stub
		 data = new Object[initialSize];
	}
	
	public boolean addToQueue(Object inputElement)throws Exception{
		if(size >= data.length){
			Object[] temp = new Object[data.length+1];
			System.arraycopy(data, 0, temp, 0, data.length);
			data = new Object[temp.length+1]; 
			System.arraycopy(temp, 0, data, 0, temp.length);
			//throw new Exception("Queue is full");
		}
		this.data[size++] = inputElement;
		return true;
	}
	
	public Object removeFromQueue()throws Exception{
		if(size < 0){
			throw new Exception("Queue is empty");
		}
		return this.data[0];
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Arrays.toString(data);
	}
}


public class MyQueue {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int initialSize = 3;//default queue size to 3
		MyOwnQueue myOwnQueue = new MyOwnQueue(initialSize);
		myOwnQueue.addToQueue(1);
		myOwnQueue.addToQueue(3);
		myOwnQueue.addToQueue(5);
		myOwnQueue.addToQueue(4);
		System.out.println(myOwnQueue.size);
		int queueType = scanner.nextInt();
		int value = scanner.nextInt();
		if(queueType == 0){
			System.out.println(myOwnQueue.removeFromQueue());
		}
		else if(queueType == 1){
			myOwnQueue.addToQueue(value);
		}
		
		System.out.println(myOwnQueue.toString().substring(1, myOwnQueue.toString().length()-1));
		
		
	}

}
