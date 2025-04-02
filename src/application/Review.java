package application;

public abstract class Review {
    private int id;
    private String content;
    private String author;
    private double averageRating;

    // Constructor
    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    // GETTER METHODS
    public int getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }
    
    public double getAverageRating() {
        return averageRating;
    }

    // UPDATE METHODS
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    
    // Abstract method to be implemented by subclasses
    public abstract String getContentType();
    public abstract int getContentId();
}