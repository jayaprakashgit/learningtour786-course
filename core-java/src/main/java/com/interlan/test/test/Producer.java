package com.interlan.test.test;

import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {

	private SharedTable sharedTable;
	
	public Producer(SharedTable sharedTable) {
		// TODO Auto-generated constructor stub
		this.sharedTable = sharedTable; 
	}
	
	public void produce(){
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for(int i=0; i<10; i++){
			executor.submit(new Task());
		}
		executor.shutdown();
	}
	
	public class Task implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				synchronized(sharedTable){
					System.out.println("Cheking whether table is empty to place the cookie...");
					while(!sharedTable.isEmpty()){
						try {
							System.out.println("table is not empty, so wait for a while");
							sharedTable.wait();
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("Yes, table is empty now, can place the cookeie...");
					sharedTable.setCookie("my cookie");
					System.out.println("placed the cookie at "+System.currentTimeMillis());
				}
			}
		}
	}
}
