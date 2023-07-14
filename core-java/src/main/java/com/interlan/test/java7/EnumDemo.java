package com.interlan.test.java7;

public class EnumDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String report = "MNGRPT";
		if(report.equals(Report.EMPRPT.getName()))
			System.out.println("Id : "+Report.EMPRPT.getId() +" Name "+Report.EMPRPT.getName());

		if(report.equals(Report.EMPRPT))
			System.out.println("Id : "+Report.EMPRPT.getId() +" Name "+Report.EMPRPT.getName());

		if(report.equals(Report.MNGRPT.toString()))
			System.out.println("Id : "+Report.MNGRPT.getId() +" Name "+Report.MNGRPT.getName());

		if(report.equals(Report.MNGRPT.name()))
			System.out.println("Id : "+Report.MNGRPT.getId() +" Name "+Report.MNGRPT.getName());

	}

}
