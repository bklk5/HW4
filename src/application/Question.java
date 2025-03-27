package application;

public class Question {
	private int id;
	private String title;
	private String content; 
	private String author;
	private String category;
	private boolean resolved;
	private int upvotes;
	
	// CREATE METHOD (constructor)
	public Question(String title, String content, String author, String category) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.category = category;
		this.upvotes = 0;
		this.resolved = false;
	}
	
	// READ METHODS 
	public int getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public int getUpvotes() {
		return this.upvotes;
	}
	
	public boolean getStatus() {
		return this.resolved;
	}
  	
	// UPDATE METHODS
	public void setId(int id) {
		this.id = id;
	}
	
	public void updateTitle(String title) {
		this.title = title;
	}
	
	public void updateAuthor(String author) {
		this.author = author;
	}
	
	public void updateContent(String content) {
		this.content = content;
	}
}
