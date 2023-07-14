package com.interlan.test.test;

import java.util.Arrays;
import java.util.Scanner;

public class AnagramTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

/*		Scanner scanner = new Scanner(System.in);
		int numberOfInput = scanner.nextInt();
		System.out.println(numberOfInput+"\n");
		for(int i =0 ; i<numberOfInput; i++){
			scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			String[] inputData = input.split(" ");
			System.out.println(isAnagram(inputData[0], inputData[1]));
		}*/
		
		System.out.println(isAnagram("aBc", "baC"));
		
	}
	
	public static boolean isAnagram(String s1, String s2){

        // Early termination check, if strings are of unequal lengths,
        // then they cannot be anagrams
        if ( s1.length() != s2.length() ) {
            return false;
        }
        s1=s1.toLowerCase();
        s2=s2.toLowerCase();
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        Arrays.sort(c1);
        Arrays.sort(c2);
        String sc1 = new String(c1);
        String sc2 = new String(c2);
        return sc1.equals(sc2);
}

}
