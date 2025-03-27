package application;

public class Answer {
	private int id;
	private int questionId;
	private String content;
	private String author;
	private int upvotes;
	
	// CREATE METHOD (constructor)
	public Answer(int questionId, String author, String content) {
		this.questionId = questionId;
		this.author = author;
		this.content = content;
	}
	
	// GETTER METHODS 
	public int getId() {
		return this.id;
	}
	
	public int getQuestionId() {
		return this.questionId;
	}

	public String getContent() {
		return this.content;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public int getUpvotes() {
		return this.upvotes;
	}
  	
	// UPDATE METHODS
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setAnswer(String content) {
		this.content = content;
	}
	
	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
	