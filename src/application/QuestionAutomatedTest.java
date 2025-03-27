package application;

public class QuestionAutomatedTest {
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	
	public static void main (String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("\nQuestion Testing Automation");
		
		/************** Start of the test cases **************/
		performTestCase(1, "Homework", "hhhhhhhhhhhhhh", "home", true);
		performTestCase(2, "Phase 1", "What is the requirement", "te", false);
		performTestCase(3, "123", "what to do", "none", false);
		performTestCase(4, "teamphase", "111", "team", false);
		performTestCase(5, "", "111", "no", false);
		performTestCase(6, "      ", "empty", "none",false);
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	public static void performTestCase(int testCase, String title, String content, String category, boolean expectedPass) {
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Title input: \"" + title + "\"");
		System.out.println("Content input: \"" + content + "\"");
		System.out.println("Category input: \"" + category + "\"");
		
		/************** Call the recognizer to process the input **************/
		String resultText = QuestionRecognizer.checkQuestion(title, content, category);
		/************** Interpret the result and display that interpreted information **************/
		if (resultText != "") {
			
			if (expectedPass) {
				System.out.println("***Failure*** The question <" + content + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
			System.out.println("***Success*** The question <" + content  + "> is invalid." + 
									"\nBut it was supposed to be invalid, so this is a pass!\n");
			System.out.println("Error message: " + resultText);
			numPassed++;
						
			}
		} else { 
			// If the test case expected the test to pass then this is a success
		if (expectedPass) {	
			System.out.println("***Success*** The question <" + content + "> is valid, so this is a pass!");
					numPassed++;
				}
						// If the test case expected the test to fail then this is a failure
			else {
			System.out.println("***Failure*** The question <" + content + 
					"> was judged as valid" + 
				"\nBut it was supposed to be invalid, so this is a failure!");
							numFailed++;
			}
		
	}
	}
	
}