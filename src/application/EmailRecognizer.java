package application;

import java.util.HashSet;
import java.util.regex.Pattern;

/**
 *  The email recognizer class handle the process of verifying emails.
 *  It make sure that email meet the following requirement:
 * 	email can not be empty
 * 	must be between 5 and 255 characters
 * 	must follow the following email pattern: ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$
 *  Must include a valid domain
 *
 * */


public class EmailRecognizer {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE
        );
	
	private static final HashSet<String> validDomains = new HashSet<>();
	
	static {
		validDomains.add(".com");
		validDomains.add(".net");
		validDomains.add(".net");
		validDomains.add(".edu");
		validDomains.add(".gov");
	}
	
	public static String checkForValidEmail(String input) {
		
		// check if length of email valid 
		if (input.length() == 0 || input.isEmpty()) {
			return "email cannot be empty";
		}
		if (input.length() < 5 || input.length() > 255) {
			return "email must be between 5 and 255 characters";
		}
		
		// check email format using regex pattern 
		if (!EMAIL_PATTERN.matcher(input).matches()) {
			return "invalid email format";
		}
		
		// check validity of domain using top level domains 
		int dotIndex = input.lastIndexOf(".");
		String domain = input.substring(dotIndex, input.length());
		
        if (!validDomains.contains(domain)) {
            return "invalid domain";
        }
        
        return "";
	}
}
