package com.interlan.test.test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MaximumDistanceCover {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int number_of_inputs = 6;
		double[] number_of_speeds = new double[]{250, 240, 230, 220, 210, 211};
		double[] number_of_fuel_consumptions = new double[]{5000, 4500, 4000, 3500, 3000, 3000};
		double total_fuel_available = 50000;
		
		double max_distance = initialize(number_of_inputs, number_of_speeds, number_of_fuel_consumptions, total_fuel_available);
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.DOWN);
		//s = df.format(max_distance);
		System.out.println(df.format(max_distance));
		
	}
	
	public static double initialize(int number_of_inputs, double[] number_of_speeds, double[] number_of_fuel_consumptions, double total_fuel_available){
		Map<Double, Double> speeds_fuel_consumptions = new HashMap<>();
		for(int i=0; i<number_of_inputs;i++){
			speeds_fuel_consumptions.put(number_of_speeds[i], number_of_fuel_consumptions[i]);
		}
		
		double max_distance = 0.0;
		
		for (Map.Entry<Double, Double> entry : speeds_fuel_consumptions.entrySet()) {
			if((total_fuel_available / entry.getValue()) * entry.getKey() > max_distance){
				max_distance = (total_fuel_available/entry.getValue()) * entry.getKey();
			}
			
		}
		return max_distance;
				
	}

}
