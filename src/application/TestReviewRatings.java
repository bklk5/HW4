package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import databasePart1.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestReviewRatings {

    private DatabaseHelper db;
    private int testReviewId;

    @BeforeEach
    void setUp() throws SQLException {
        db = new DatabaseHelper();
        db.connectToDatabase();

        // Clean old test review
        db.getConnection().createStatement().execute("DELETE FROM ReviewRatings WHERE student_username LIKE 'student%'");
        db.getConnection().createStatement().execute("DELETE FROM Reviews WHERE author = 'testAuthor'");

        // Insert a test review
        QuestionReview review = new QuestionReview(1, "testAuthor", "This is a test review.");
        db.createQuestionReview(review);

        // Retrieve its ID
        List<QuestionReview> reviews = db.getQuestionReviewsByQuestionId(1);
        for (QuestionReview r : reviews) {
            if (r.getAuthor().equals("testAuthor")) {
                testReviewId = r.getId();
                break;
            }
        }
    }

    /**
	 * Test 1: That a student can rate a review for the first time and the average is stored correctly.
	 */
    
    @Test
    void testAddNewRating() throws SQLException {
        db.addOrUpdateReviewRating(testReviewId, "student1", 8);
        Map<Integer, Double> ratings = db.getAverageRatingsForAllReviews();
        assertTrue(ratings.containsKey(testReviewId));
        assertEquals(8.0, ratings.get(testReviewId), 0.1);
    }

    /**
	 * Test 2: That updating a rating for the same review by the same student works so, not inserted twice.
	 */
    
    @Test
    void testUpdateExistingRating() throws SQLException {
        db.addOrUpdateReviewRating(testReviewId, "student1", 6);
        db.addOrUpdateReviewRating(testReviewId, "student1", 9);
        Map<Integer, Double> ratings = db.getAverageRatingsForAllReviews();
        assertEquals(9.0, ratings.get(testReviewId), 0.01);
    }

    /**
	 * Test 3: That multiple students can rate the same review and the average is calculated correctly.
	 */
    
    @Test
    void testMultipleRatingsAverage() throws SQLException {
        db.addOrUpdateReviewRating(testReviewId, "studentA", 7);
        db.addOrUpdateReviewRating(testReviewId, "studentB", 10);
        db.addOrUpdateReviewRating(testReviewId, "studentC", 9);
        Map<Integer, Double> ratings = db.getAverageRatingsForAllReviews();
        double avg = ratings.get(testReviewId);
        assertEquals(8.67, Math.round(avg * 100.0) / 100.0, 0.01);
    }
    
    /**
   	 * Test 4: That if no one rated a review (or a non-existent review), it doesn’t falsely show an average.
   	 */
    
    @Test
    void testNoRatingsPresent() throws SQLException {
        int unknownReviewId = 9999;
        Map<Integer, Double> ratings = db.getAverageRatingsForAllReviews();
        assertFalse(ratings.containsKey(unknownReviewId));
    }

    /**
   	 * Test 5: That trying to rate a nonexistent review ID throws a proper exception.
   	 */
    
    @Test
    void testRateNonexistentReview() {
        assertThrows(SQLException.class, () -> {
            db.addOrUpdateReviewRating(9999, "ghostUser", 5);
        });
    }
    
    /**
   	 * Test 6: That each student can only rate a review once — updating overwrites, not duplicates.
   	 */
    
    @Test
    void testUniqueStudentRatingConstraint() throws SQLException {
        db.addOrUpdateReviewRating(testReviewId, "uniqueStudent", 5);
        db.addOrUpdateReviewRating(testReviewId, "uniqueStudent", 10); // should update, not insert new
        Map<Integer, Double> ratings = db.getAverageRatingsForAllReviews();
        assertEquals(10.0, ratings.get(testReviewId), 0.01);
    }
}
