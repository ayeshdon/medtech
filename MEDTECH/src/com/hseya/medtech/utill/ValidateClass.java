package com.hseya.medtech.utill;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateClass {


	/*
	 * valid email address
	 */

	public static boolean isValidEmailAddress(String email){
		boolean flag=false;
		try {
			Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher m = p.matcher(email);
			boolean matchFound = m.matches();

			if (matchFound){

				System.out.println("okkkkkkkkkkkkkkkkkkkkk");
				flag = true;
			}
			else{

				System.out.println("dddddddddddddddddddddd");
				flag = false;

			}

			System.out.println("return value "+flag);

			return flag;
		} catch (Exception e) {
			System.out.println("eeeeeeeeeeeeeeeeeeeeeeee");
			return false;
		}
	}


}
