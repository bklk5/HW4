package application;
import java.util.ArrayList;
import java.util.List;

public class QuestionsList {
	private List<Question> questions;
	
	public QuestionsList() {
		this.questions = new ArrayList<>();
	}
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public List<Question> getQuestions() {
		return this.questions;
	}
	
	public void addQuestion(Question question) {
		questions.add(question);
	}
	
	public void removeQuestion(int id) {
		for(Question q : questions) {
			if (q.getId() == id) {
				questions.remove(q);
			}
		}
	}
	
	// search questions by category
	public List<Question> searchCategory(String catgeory) {
		List<Question> result = new ArrayList<>();
		
		for (Question q : questions) {
			String currentCategory = q.getCategory();
			
			if (currentCategory.equals(catgeory.toLowerCase())) {
				result.add(q);
			}
		}
		
		return result;
	}
	
	// search questions by keyword in title
	public List<Question> searchKeyword(String words) {
		List<Question> result = new ArrayList<>();
		
		for (Question q : questions) {
			String currentTitle = q.getTitle().toLowerCase();
			
			if (currentTitle.contains(words.toLowerCase())) {
				result.add(q);
			}
		}
		
		return result;
				
	}
}
