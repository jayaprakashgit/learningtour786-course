package com.interlan.test.dp;

public class TestStrategyPatternDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Engine engine = new Engine(new FuelConsuptionStrategy());
		engine.buildEngine();
		
		engine = new Engine(new HighSpeedEngineStrategy());
		engine.buildEngine();
			
	}

}
