package application;

public class Review {
	private int id;
	private int questionId;
	private String content;
	private String author;
	
	// CREATE METHOD (constructor)
	public Review(int questionId, String author, String content) {
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
  	
	// UPDATE METHODS
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setAnswer(String content) {
		this.content = content;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	//RATING METHODS
	private double averageRating;

	public double getAverageRating() {
	    return averageRating;
	}

	public void setAverageRating(double averageRating) {
	    this.averageRating = averageRating;
	}
}
	