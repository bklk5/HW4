package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import databasePart1.*;

class TestReviewsCRUD {
    private DatabaseHelper databaseHelper;
    private int questionId;
    private int answerId;

    @BeforeEach
    void setUp() throws SQLException {
        databaseHelper = new DatabaseHelper();
        databaseHelper.connectToDatabase();

        // Create test user
        User testUser = new User("Test User", "test@example.com", "testAuthor", "password", false, true, false, false, true);
        if (!databaseHelper.doesUserExist("testAuthor")) {
            databaseHelper.register(testUser);
        }

        // Create test question
        Question testQuestion = new Question("Test Question", "This is a test question content", "testAuthor", "Test");
        databaseHelper.createQuestion(testQuestion);
        
        // Get question ID
        List<Question> questions = databaseHelper.getQuestionByAuthor("testAuthor");
        if (!questions.isEmpty()) {
            questionId = questions.get(questions.size() - 1).getId();
        }

        // Create test answer
        Answer testAnswer = new Answer(questionId, "testAuthor", "This is a test answer content");
        databaseHelper.createAnswer(testAnswer);
        
        // Get answer ID
        List<Answer> answers = databaseHelper.readAnswersByQuestionId(questionId);
        if (!answers.isEmpty()) {
            answerId = answers.get(answers.size() - 1).getId();
        }
    }

    @AfterEach
    void disconnect() {
        try {
            databaseHelper.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test 1: Create a question review
     */
    @Test
    void testCreateQuestionReview() {
        QuestionReview qr = new QuestionReview(questionId, "testAuthor", "This is a test for question reviews.");
        assertTrue(databaseHelper.createQuestionReview(qr));
    }

    /**
     * Test 2: Update a question review
     */
    @Test
    void testUpdateQuestionReview() throws SQLException {
        // First create a review
        QuestionReview qr = new QuestionReview(questionId, "testAuthor", "This is a test for question reviews.");
        databaseHelper.createQuestionReview(qr);
        
        // Get the review ID
        List<QuestionReview> reviews = databaseHelper.getQuestionReviewsByQuestionId(questionId);
        if (!reviews.isEmpty()) {
            int reviewId = reviews.get(reviews.size() - 1).getId();
            assertTrue(databaseHelper.updateQuestionReview(reviewId, "Updated question review content"));
        }
    }

    /**
     * Test 3: Delete a question review
     */
    @Test
    void testDeleteQuestionReview() throws SQLException {
        // First create a review
        QuestionReview qr = new QuestionReview(questionId, "testAuthor", "This is a test for question reviews.");
        databaseHelper.createQuestionReview(qr);
        
        // Get the review ID
        List<QuestionReview> reviews = databaseHelper.getQuestionReviewsByQuestionId(questionId);
        if (!reviews.isEmpty()) {
            int reviewId = reviews.get(reviews.size() - 1).getId();
            assertTrue(databaseHelper.deleteQuestionReview(reviewId));
        }
    }

    /**
     * Test 4: Create an answer review
     */
    @Test
    void testCreateAnswerReview() {
        AnswerReview ar = new AnswerReview(answerId, "testAuthor", "This is a test for answer reviews.");
        assertTrue(databaseHelper.createAnswerReview(ar));
    }

    /**
     * Test 5: Update an answer review
     */
    @Test
    void testUpdateAnswerReview() throws SQLException {
        // First create a review
        AnswerReview ar = new AnswerReview(answerId, "testAuthor", "This is a test for answer reviews.");
        databaseHelper.createAnswerReview(ar);
        
        // Get the review ID
        List<AnswerReview> reviews = databaseHelper.getAnswerByAnswerId(answerId);
        if (!reviews.isEmpty()) {
            int reviewId = reviews.get(reviews.size() - 1).getId();
            assertTrue(databaseHelper.updateAnswerReview(reviewId, "Updated answer review content"));
        }
    }

    /**
     * Test 6: Delete an answer review
     */
    @Test
    void testDeleteAnswerReview() throws SQLException {
        // First create a review
        AnswerReview ar = new AnswerReview(answerId, "testAuthor", "This is a test for answer reviews.");
        databaseHelper.createAnswerReview(ar);
        
        // Get the review ID
        List<AnswerReview> reviews = databaseHelper.getAnswerByAnswerId(answerId);
        if (!reviews.isEmpty()) {
            int reviewId = reviews.get(reviews.size() - 1).getId();
            assertTrue(databaseHelper.deleteAnswerReview(reviewId));
        }
    }
}