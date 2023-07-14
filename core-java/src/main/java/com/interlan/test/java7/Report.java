package com.interlan.test.java7;

public enum Report {

	EMPRPT(1, "EMPLOYEE_REPORT"), MNGRPT(2, "MANAGER_REPORT");
	private String name;
	private int id;
	Report(int id, String name){
		this.setName(name);
		this.id=id;
	}
	
	public String getName(){return name;}
	
	public void setName(String name){this.name = name;}
	
	public int getId(){return this.id;}
}
