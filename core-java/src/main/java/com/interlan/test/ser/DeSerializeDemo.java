package com.interlan.test.ser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DeSerializeDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			FileInputStream fis = new FileInputStream("C:/JP/Interview/Java/InterviewReview/src/com/interview/ser/emp.abc");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Employee employee = (Employee) ois.readObject();
			System.out.println(employee);
			System.out.println(employee.name);
			System.out.println(employee.number);
			System.out.println(employee.SSN);
			System.out.println(employee.address);
			System.out.println(employee.parentId);
			System.out.println(employee.parentAge);
			System.out.println(employee.addressObj.streetName);
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
	}

}
