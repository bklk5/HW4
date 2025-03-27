package application;

import java.util.Random;

/**
 * OnetimePassword class generates a one-time password 
 */

public class OnetimePasswordGenerator{
	
	public static String random_password () {
		// new random password includes: capital char, lower case char, number and symbol
		String capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lower_chars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "~`!@#$%^&*()_-+={}[]|\\\\:;\\\"'<>,.?/";
		
		String chars = capital_chars + lower_chars + numbers + symbols;
		
		// random method to generate a new onetime password
		Random rndm_method = new Random();
		int password_length = 6;
		char[] new_password = new char[password_length];
		
		// each index is given a random char using charAt() and nextInt()
		for (int i = 0; i < password_length; i++){
			new_password[i] = chars.charAt(rndm_method.nextInt(chars.length()));
		}
		String str_new_password = new String (new_password);
		return str_new_password;
	}

	public static void main(String[] args){
		String pass = random_password();
		System.out.println(pass);
	}
}
