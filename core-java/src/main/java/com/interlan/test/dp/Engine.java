package com.interlan.test.dp;

public class Engine {
	
	private Strategy strategy;
	
	public Engine(Strategy strategy) {
		// TODO Auto-generated constructor stub
		this.strategy = strategy;
	}
	
	public void buildEngine(){
		this.strategy.setupEngine();
	}
}
