package application;

import java.util.ArrayList;
import java.util.List;

public class AnswersList {
	private List<Answer> answers;
	
	public AnswersList() {
		this.answers = new ArrayList<>();
	}
	
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public List<Answer> getAnswers() {
		return this.answers;
	}
	
	public void addAnswer(Answer answer) {
		answers.add(answer);
	}
	
	public void removeAnswer(int id) {
		for(Answer a : answers) {
			if (a.getId() == id) {
				answers.remove(a);
			}
		}
	}
	
	// search answer by keyword in title
	public List<Answer> searchContent(String words) {
		List<Answer> result = new ArrayList<>();
		
		for (Answer a : answers) {
			String currentContent = a.getContent().toLowerCase();
			
			if (currentContent.contains(words.toLowerCase())) {
				result.add(a);
			}
		}
		
		return result;
	}
}
