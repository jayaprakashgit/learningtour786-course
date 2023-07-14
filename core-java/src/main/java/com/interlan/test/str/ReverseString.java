package com.interlan.test.str;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ReverseString {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//String input = br.readLine();
		String input ="jaya";
		//System.out.println(reverseUsingToChar(input));
		//System.out.println(reverseUsingStringBuffer(input));
		//reverseUsingLinkedList(input);
		reverseUsingTemp(input);
	}
	
	
	public static String reverseUsingToChar(String input){
		char[] chArray = input.toCharArray();
		char[] resArray = new char[chArray.length];
		int j=0;
		for (int i=chArray.length-1; i>=0; i--) {
			resArray[j++]=chArray[i];
		}
		return Arrays.toString(resArray);
	}

	public static String reversUsingStringBuffer(String input){
		return new StringBuffer(input).reverse().toString();
	}
	

	public static void reverseUsingLinkedList(String input){
		char[] inputArray = input.toCharArray();
		LinkedList<Character> linkedList = new LinkedList<>();
		for(int i=0; i<inputArray.length; i++){
			linkedList.add(inputArray[i]);
		}
		Collections.reverse(linkedList);
		
		for (Character listItr : linkedList) {
			System.out.print(listItr.toString());
		}
	}
	
	
	public static void reverseUsingTemp(String input){
		char[] inputArray = input.toCharArray();
		int left,right = 0;
		right = inputArray.length-1;
		char temp;
		for(int lft=0; lft<right; lft++,right--){
			temp = inputArray[right];
			inputArray[right] = inputArray[lft];
			inputArray[lft] = temp;
		}
		
		for (char c : inputArray) {
			System.out.print(c);
		}
	}


}
