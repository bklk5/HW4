package application;

public class SearchRecognizer {
	
	public static String checkSearch(String content) {
		
		// check if length of email valid 
		if (content.isEmpty()) {
			return "content field cannot be empty";
		}
		
		if (content.length() < 5 || content.length() > 100) {
			return "content must be between 5 and 100 characters";
		}
        
		if (content.trim().isEmpty()) {
			return "content cannot contain only spaces";
		}
        return "";
	}
}
