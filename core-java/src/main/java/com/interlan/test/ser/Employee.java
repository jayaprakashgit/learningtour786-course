package com.interlan.test.ser;

public class Employee extends Parent implements java.io.Serializable{

	   public String name;
	   public String address;
	   public transient int SSN;
	   public int number;
	   public Address addressObj;
	   public void mailCheck()
	   {
	      System.out.println("Mailing a check to " + name
	                           + " " + address);
	   }
	   
}
