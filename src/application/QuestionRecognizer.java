package application;

public class QuestionRecognizer {
	public static String checkQuestion(String title, String content, String category) {
		
		if (title.length() == 0 || content.length() == 0 || category.length() == 0) {
			return "title, content, and category fields cannot be empty";
		}
		
		if (title.length() < 5 || title.length() > 150) {
			return "titles must be between 5 and 150 characters";
		}
		
		if (content.length() < 5 || content.length() > 1500) {
			return "content must be between 5 and 1500 characters";
		}
		
		if (category.length() < 3 || category.length() > 50) {
			return "category must be between 3 and 50 characters";
		}
		
		if (title.trim().isEmpty() || content.trim().isEmpty() || category.trim().isEmpty()) {
			return "title, content, and category fields cannot contain only spaces";
		}
        
        return "";
	}
}
