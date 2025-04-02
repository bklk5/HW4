package application;

public class QuestionReview extends Review {
    private int questionId;

    public QuestionReview(int questionId, String author, String content) {
        super(author, content);
        this.questionId = questionId;
    }

    @Override
    public String getContentType() {
        return "question";
    }

    @Override
    public int getContentId() {
        return questionId;
    }
    
    public int getQuestionId() {
        return questionId;
    }
}