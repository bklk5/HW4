package application;

public class AnswerRecognizer {
	public static String checkAnswer(String content) {
		
		// check if length of email valid 
		if (content.isEmpty()) {
			return "content field cannot be empty";
		}
		
		if (content.length() < 5 || content.length() > 1500) {
			return "content must be between 5 and 1500 characters";
		}
		
		if(content.trim().isEmpty()) {
			return "content cannot contain only spaces";
		}
        
        return "";
	}
}
