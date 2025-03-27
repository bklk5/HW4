package application;

// Original author: Lynn Robert Carter
// Updated by Charly Rapin


/**
 * The password evaluator class handles the setup to process the verification of the password
 * before creating a new user.
 * The password evaluator check that the password meet the following requirement:
 * Must have one lower case character, a-z
 * Must have one upper case character, A-z
 * Must have one number, 0-9
 * Must have one special character: ~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/
 * Lastly, it must be bigger than 7 characters 
 * */


public class PasswordEvaluator {
	
	public static String passwordErrorMessage = "";		
	public static String passwordInput = "";			
	public static int passwordIndexofError = -1;		
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	public static boolean otherChar = false; 
	private static String inputLine = "";				
	private static char currentChar;					
	private static int currentCharNdx;					
	private static boolean running;	
	private static int state = 0;						
	private static int nextState = 0;

	/**********
	 * This private method display the input line and then on a line under it displays an up arrow
	 * at the point where an error should one be detected.  This method is designed to be used to 
	 * display the error message on the console terminal.
	 * 
	 * @param input				The input string
	 * @param currentCharNdx	The location where an error was found
	 * @return					Two lines, the entire input line followed by a line with an up arrow
	 */
	private static void displayInputState() {
		// Display the entire input line
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?");
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
	}

	/**********
	 * 
	 * @param input		The input string for directed graph processing
	 * @return			An output string that is empty if the password is valid or it will be
	 * 					a string with a help description of the error 
	 */
	public static String evaluatePassword(String input) {
		// The following are the local variable used to perform the Directed Graph simulation
		passwordErrorMessage = "";
		passwordIndexofError = 0;			
		inputLine = input;					
		currentCharNdx = 0;
		
		// Reset Boolean flag
		foundUpperCase = false;				
		foundLowerCase = false;				
		foundNumericDigit = false;			
		foundSpecialChar = false;			
		foundNumericDigit = false;			
		foundLongEnough = false;
		otherChar = false;
		
		if(input.length() <= 0) return "*** ERROR *** Password is Empty";
		
		// The input is not empty, so we can access the first character
		currentChar = input.charAt(0);		

		// Save copy of input 
		passwordInput = input;				
		
		
		// Current state of the FSM
		running = true;
		nextState = 0;
		state = 0; 

		// The Directed Graph simulation continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition
		while (running) {
			
			// The cascading if statement sequentially tries the current character against all of the
			// valid transitions
			switch (state) {
			case 0:
				
				if (currentChar >= 'A' && currentChar <= 'Z') {
					foundUpperCase = true;
					
				} 	else if (currentChar >= 'a' && currentChar <= 'z') {
					foundLowerCase = true;
					
				} else if (currentChar >= '0' && currentChar <= '9') {
					foundNumericDigit = true;
					
				} else if ("~`!@#$%^&*()_-+{}[]|:,.?/".indexOf(currentChar) >= 0) {
					foundSpecialChar = true;
					
				} else {
					// Invalid character, transition to state 1
					passwordIndexofError = currentCharNdx;
					otherChar = true;
					nextState = 1;
				}
				if (currentCharNdx >= 7) {
					foundLongEnough = true;
					
				}
			
				// Go to the next character if there is one
				currentCharNdx++;
				if (currentCharNdx >= inputLine.length())
					// Input fully consumed, transition to state 1
					nextState = 1;
				else
					currentChar = input.charAt(currentCharNdx);
			
				break;
				
			case 1:
				// In state one, no more input accepted
				// Exit the loop
				running = false;
				break;
				
			}
			if(running) 
				state = nextState;
		}
		
		// Create error message based on the requirement missing
		String errMessage = "";
		// The current state of the FSM is now 1
		// 2 ways to end up in the final state 1
		// Other char is true or the input is fully consumed
		if(otherChar)
			return "*** ERROR *** An invalid character has been found!";
		
		// There are no invalid character, check rest of requirements
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
			
		if (!foundSpecialChar)
			errMessage += "Special character; ";
			
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		
		if (errMessage == "")
			return "";
		
		passwordIndexofError = currentCharNdx;
		// Return the finalized error message 
		return errMessage + "conditions were not satisfied";
	}



}
