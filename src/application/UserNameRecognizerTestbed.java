package application;

import java.util.Scanner;

/*******
 * The mainline of a console terminal implementation of an UserName
 * Recognizer testbed
 */

public class UserNameRecognizerTestbed{
	

	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("\nUserName Testing Automation");
		
		/************** Start of the test cases **************/
		performTestCase(1, "abcd", true);
		performTestCase(2, "adadaaaaaaaaaaaa", true);
		performTestCase(3, "3wwD", false);
		performTestCase(4, "DD_s3", true);
		performTestCase(5, "s.3D",true);
		performTestCase(6, "D3-a",true);
		performTestCase(7,"G--a",false);
		performTestCase(8, "a-s",false);
		performTestCase(9, "aaaaaaaaaaaaaaaaa",false);
		performTestCase(10, "Dwq3=",false);
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	public static void performTestCase (int testCase, String content, boolean expectedPass) {
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Contents input: \"" + content + "\"");
		
		/************** Call the recognizer to process the input **************/
		String resultText = UserNameRecognizer.checkForValidUserName(content);
		/************** Interpret the result and display that interpreted information **************/
		if (resultText != "") {
			
			if (expectedPass) {
				System.out.println("***Failure*** The email <" + content + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
			System.out.println("***Success*** The email <" + content  + "> is invalid." + 
									"\nBut it was supposed to be invalid, so this is a pass!\n");
			System.out.println("Error message: " + resultText);
			numPassed++;
						
			}
		} else { 
			// If the test case expected the test to pass then this is a success
		if (expectedPass) {	
			System.out.println("***Success*** The email <" + content + "> is valid, so this is a pass!");
					numPassed++;
				}
						// If the test case expected the test to fail then this is a failure
			else {
			System.out.println("***Failure*** The email <" + content + 
					"> was judged as valid" + 
				"\nBut it was supposed to be invalid, so this is a failure!");
							numFailed++;
			}
			
		}
	}
}