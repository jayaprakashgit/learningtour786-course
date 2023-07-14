package com.interlan.test.test;

import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {

	private SharedTable sharedTable;
	
	public Consumer(SharedTable sharedTable) {
		// TODO Auto-generated constructor stub
		this.sharedTable = sharedTable;
	}
	
	public void consume(){
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for(int i=0; i<10; i++){
			executorService.submit(new Worker());
		}
		executorService.shutdown();
	}
	
	public class Worker implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				synchronized (sharedTable) {
					while(sharedTable.isEmpty()){
						try {
							System.out.println("sharedtable is empty, so wait for cookie to avaiable...");
							sharedTable.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("eating cookie called : "+sharedTable.eatCookie());
					System.out.println("ate cookie at "+Clock.systemDefaultZone());
					sharedTable.notify();
				}
			}
		}
	}
}
