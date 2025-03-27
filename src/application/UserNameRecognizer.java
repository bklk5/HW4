package application;

// Original author: Lynn Robert Carter
// Updated by Charly Rapin
/**
 *  The username recognizer class handle the process of verifying usernames.
 *  It make sure that username meet the following requirement:
 *  First character must be alphabetic.
 *  Only A-Z,a-z,0-9 (UNChar) characters are allowed with the exception of the following special characters:
 *  A period, minus sign and underscore.
 *  Special characters may only be used between two UNChar characters.
 *  The user name must be longer then 3 characters and shorter then 17 characters.
 * */


public class UserNameRecognizer {
	

	public static String userNameRecognizerErrorMessage = "";	
	public static String userNameRecognizerInput = "";			
	public static int userNameRecognizerIndexofError = -1;		
	private static int state = 0;						
	private static int nextState = 0;					
	private static boolean finalState = false;			
	private static String inputLine = "";				
	private static char currentChar;					
	private static int currentCharNdx;					
	private static boolean running;
	
	
														
	private static int userNameSize = 0;			

	// Private method to display debugging data
	private static void displayDebuggingInfo() {
		// Display the current state of the FSM as part of an execution trace
		if (currentCharNdx >= inputLine.length())
			// display the line with the current state numbers aligned
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "None");
		else
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
				nextState + "     " + userNameSize);
	}
	
	// Private method to move to the next character within the limits of the input line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that says Valid Username if the username satisfies every requirements or a string
	 * 					with an error message
	 */
	public static String checkForValidUserName(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			// Error at first character
			userNameRecognizerIndexofError = 0;
		
			return "\n*** ERROR *** The input is empty";
		}
		
		// The local variables used to perform the Finite State Machine simulation
		// There is the FSM state number, a reference to the input line made global,
		// Index of the currrent character and the character itself
		
		state = 0;							
		inputLine = input;					
		currentCharNdx = 0;					
		currentChar = input.charAt(0);		

		userNameRecognizerInput = input;	// Save a copy of the input
		running = true;						
		nextState = -1;						// Next state unknow until the first character is checked
	
		
		
		userNameSize = 0;					

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			
			switch (state) {
			case 0: 
				
				// The first character is checked against A-Z, a-z. The first character must be alphabetic.
				// If input matches
				// The FSM then goes to state 1
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		
						(currentChar >= 'a' && currentChar <= 'z' ) )	
						 {	
					nextState = 1;
					
					// Count the character 
					userNameSize++;
					
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
					
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has two valid transitions, 
				//	1: a A-Z, a-z, 0-9 that transitions back to state 1
				//  2: a period, minus sign, underscore that transitions to state 2 

				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		
						(currentChar >= 'a' && currentChar <= 'z' ) ||	
						(currentChar >= '0' && currentChar <= '9' )) {	
					nextState = 1;
					
				
					// Count the character
					userNameSize++;
				}
				// ., -, _ -> State 2
				else if (currentChar == '.' || currentChar == '-'|| currentChar == '_') {							
					nextState = 2;
					
					// Count the special character 
					userNameSize++;
				}				
				// If it is none of those characters, the FSM halts
				else
					running = false;
					
				
				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
				
			case 2: 
				// State 2 deals with a character after a period, minus sign or underscore in the name
				
				// A-Z, a-z, 0-9 -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		
						(currentChar >= 'a' && currentChar <= 'z' ) ||	
						(currentChar >= '0' && currentChar <= '9' )) {	
					nextState = 1;
					
					// Count the odd digit
					userNameSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				 	

				// The execution of this state is finished
				// If the size is larger than 16, the loop must stop
				if (userNameSize > 16)
					running = false;
				break;			
			}
			
			if (running) {
			
				// When the processing of a state is done, the FSM proceeds to the next
				// character in the input and fetches that character and updates the currentChar.  
				// If there is no next character the currentChar is set to a blank.
				
				moveToNextCharacter();

				// Move to the next state
				state = nextState;
				
				// Check if the new state is a final state
				if (state == 1) finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
	
		}
		
		
		System.out.println("The loop has ended.");
		
		// FSM halts, determining if the situation is an error or not. It depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states 
		// to display a very specific error message 
		
		userNameRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		userNameRecognizerErrorMessage = "\n*** ERROR *** ";
		
		
		switch (state) {
		case 0:
			// State 0 is not a final state, therefore the username is not valid
			// The first character was not one of the following: A-Z, a-z

			userNameRecognizerErrorMessage += "A UserName must start with A-Z, a-z.\n";
			return userNameRecognizerErrorMessage;

		case 1:
			// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
			// we must ensure the whole string has been consumed.

			if (input.length() < 4) {
				// UserName is too small
				userNameRecognizerErrorMessage += "A UserName must have at least 4 characters.\n";
				
				return userNameRecognizerErrorMessage;
			}
			else if (userNameSize > 16) {
				// UserName is too long
				userNameRecognizerErrorMessage += 
					"A UserName must have no more than 16 character.\n";
				
				return userNameRecognizerErrorMessage;
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				userNameRecognizerErrorMessage += 
					"A UserName character may only contain the characters A-Z, a-z, 0-9, ., -, _\n";
				return userNameRecognizerErrorMessage;
			}
			else {
					// UserName is valid
					userNameRecognizerIndexofError = -1;
					userNameRecognizerErrorMessage = "";
					return userNameRecognizerErrorMessage;
			}

		case 2:
			// State 2 is not a final state, username not valid
			// The next character was not one of the following: A-Z, a-z, 0-9
			userNameRecognizerErrorMessage +=
				"A UserName character after a period, minus sign or underscore must be A-Z, a-z, 0-9.\n";
			return userNameRecognizerErrorMessage;
			
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}
}
