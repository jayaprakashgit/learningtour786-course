package com.interlan.test.oops;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Parent p = new Child();
		Child c = (Child)new Parent();*/
		
		String a = null;
		try{
			System.out.println(a.length());
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally {
			a = "String";
			System.out.println(a.length());
		}
	}

}
