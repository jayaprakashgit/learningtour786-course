package com.interlan.test.ser;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	      Employee emp = new Employee();
	      emp.name = "Reyan Ali";
	      emp.address = "Phokka Kuan, Ambehta Peer";
	      emp.SSN = 11122333;
	      emp.number = 101;
	      
	      try{
	    	  FileOutputStream fout = new FileOutputStream("C:/JP/Interview/Java/InterviewReview/src/com/interview/ser/emp.abc");
	    	  ObjectOutputStream oout = new ObjectOutputStream(fout);
	    	  oout.writeObject(emp);
	    	  oout.close();
	    	  fout.close();
	    	  System.out.println(emp);
	    	  System.out.println("Serializable the emp data into 'C:/JP/Interview/Java/InterviewReview/src/com/interview/ser/emp.ser' location");
	      }
	      catch(IOException ioe){
	    	  
	      }
	      
	}

}
