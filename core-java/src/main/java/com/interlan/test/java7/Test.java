package com.interlan.test.java7;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class Test implements I2{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(I2.name+"");
		System.out.println(I2.s1+"");
		System.out.println(((I1)new Test()).name);
		
		Long l = new Long(1234);
		Long l1 = l;
		System.out.println(l+","+l1);
/*		
		Long l = new Long(1234);
		long l1 = 1234;*/
		
		if(l1==l)
			System.out.println("equalllll");
		else
			System.out.println("not equalllll");

		System.out.println("------------------------");
		l++;
		if(l==l1)
			System.out.println("equalllll");
		else
			System.out.println("not equalllll");

		
		int c = 0;
		System.out.println((0==c++) ? "true":"false");

		Double d = null;
		System.out.println(d instanceof Double);
		
		int data =2;
		Helper h = new Helper();
		h.bump(data);
		System.out.println(h.data);
		System.out.println(data);
		
		String strA= "A";
		String strB= "B";
		StringBuilder bufferc = new StringBuilder("C");
		
		
		Formatter fmt = new Formatter(bufferc);
		fmt.format("%s%s", strA, strB);
		System.out.println(fmt);
		
		fmt.format("%-2s", strB);
		System.out.println(fmt);
		
		fmt.format("%b", null);
		System.out.println(fmt);
		
		Console cons =  System.console();
		System.out.println("----------------------");
		try {
			Date date = null;
			date = new SimpleDateFormat("yyyy-mm-dd").parse("2012-01-15");
			Calendar ac = Calendar.getInstance();
			ac.setTime(date);
			System.out.println(ac.get(ac.DAY_OF_MONTH));
			System.out.println(ac.get(ac.MONTH));
			
			Double dbl = new Double("1.4d");
			Float f = new Float(12);
			//Integer in= new Integer();
			Boolean bb = new Boolean("false");
			
			Integer x = 3;
			Integer y = null;
			System.out.println(x.compareTo(3) == 0 || y.compareTo(0) == 0);
			//System.out.println(y.compareTo(0) == 0 || x.compareTo(3) == 0);
			//System.out.println(y.compareTo(null)==0 || true);
			
			System.out.println(1==1 && y.compareTo(0) == 0);//&& will check the first condition if satisfy, then it moves to next condition, whereas & will go thru all the condition
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static class Helper{
		static int tot = 5;
		private int data =5;
		public void bump(int inc){
			int tot = 10;
			inc++;
			data = data + inc;
			System.out.println(this.tot);
			
		}
		protected int abc(){
			return 0;
		}
	}

/*	@Override
	public void myNonStaticMethod() {
		// TODO Auto-generated method stub
		
	}*/
	
/*	static class Helper1 extends Helper{
		@Override
		public long abc(){
			return 0;
		}
	}*/

}
