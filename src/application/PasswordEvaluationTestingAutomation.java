package application;


public class PasswordEvaluationTestingAutomation {
	
	static int numPassed = 0;	
	static int numFailed = 0;	

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	
	public static void main(String[] args) {
		
		System.out.println("______________________________________");
		System.out.println("\nPassword Testing Automation");

		// Test cases
		
		// #1 
		performTestCase(1, "Aa!15678", true);
		
		// #2
		performTestCase(2, "3!aaaaa8", false);
		
		// #3
		performTestCase(3, "V$156786", false);
		
		// #4
		performTestCase(4, "o1G23IIH", false);
		
		// #5
		performTestCase(5, "*^()_Pkz", false);
		
		// # 6 
		performTestCase(6, "@3QFa989=", false);
		
		// # 7
		performTestCase(7, "!@3QFa9", false);
		// #8
		performTestCase(8, "Aa!15678gf~`!@#$%^&*()_-+{}[]|:,.?/", true);
		
		
		// End of test cases
		
		// Return the amount of pass and fail test
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
		// Display an individual test case header 
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		
		// Verifying the given password
		String resultText= PasswordEvaluator.evaluatePassword(inputText);
		
		// Interpreting result, verifying if the password evaluator gave the correct result
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {
		if (PasswordEvaluator.foundUpperCase)
			System.out.println("At least one upper case letter - Satisfied");
		else
			System.out.println("At least one upper case letter - Not Satisfied");

		if (PasswordEvaluator.foundLowerCase)
			System.out.println("At least one lower case letter - Satisfied");
		else
			System.out.println("At least one lower case letter - Not Satisfied");
	

		if (PasswordEvaluator.foundNumericDigit)
			System.out.println("At least one digit - Satisfied");
		else
			System.out.println("At least one digit - Not Satisfied");

		if (PasswordEvaluator.foundSpecialChar)
			System.out.println("At least one special character - Satisfied");
		else
			System.out.println("At least one special character - Not Satisfied");

		if (PasswordEvaluator.foundLongEnough)
			System.out.println("At least 8 characters - Satisfied");
		else
			System.out.println("At least 8 characters - Not Satisfied");
		
		if(!PasswordEvaluator.otherChar)
			System.out.println("No invalid characters - Satisfied");
		else
			System.out.println("No invalid characters - Not Satisfied");
	}
}
