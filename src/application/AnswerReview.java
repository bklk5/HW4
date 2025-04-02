package application;

public class AnswerReview extends Review {
    private int answerId;

    public AnswerReview(int answerId, String author, String content) {
        super(author, content);
        this.answerId = answerId;
    }

    @Override
    public String getContentType() {
        return "answer";
    }

    @Override
    public int getContentId() {
        return answerId;
    }
    
    public int getAnswerId() {
        return answerId;
    }
}