package com.interlan.test.test;

public class TestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SharedTable sharedTable = new SharedTable();
		Producer producer = new Producer(sharedTable);
		Consumer consumer = new Consumer(sharedTable);
		producer.produce();
		consumer.consume();
	}

}
