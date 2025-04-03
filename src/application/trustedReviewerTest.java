package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import databasePart1.*;

class trustedReviewerTest {
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
     // Create test student
        User testStudent = new User("Test Student", "stu@gamil.com", "testStu", "password", false, true, false, false, true);
        if (!databaseHelper.doesUserExist("testStu")) {
            databaseHelper.register(testUser);
        }
        // Create test reviewer
        User testReviewer = new User("Test Reviewer", "rev@gamil.com", "testRev", "password", false, true, false, false, true);
        if (!databaseHelper.doesUserExist("testRev")) {
            databaseHelper.register(testUser);
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
     * Test 1: See all reviewers
     */
    @Test
    void testgetAllReviewers() throws SQLException {
    	List<String> allReviewers = databaseHelper.getAllReviewers();
    	assertTrue(allReviewers.contains("testReviewer"));  
    	}

    /**
     * Test 2: add trusted reviewers
     */
    @Test
    void testaddTrustedReviewer() throws SQLException {
    	boolean added = databaseHelper.addTrustedReviewer("testStudent", "testReviewer");

        assertTrue(added);
        assertTrue(databaseHelper.getTrustedReviewers("testStudent").contains("testReviewer")); 
    	}
    /**
     * Test 3: See trusted reviewer list 
     */
    @Test
    public void testgetTrustedReviewers() throws SQLException {
        databaseHelper.addTrustedReviewer("testStudent", "testReviewer");
        List<String> trustedreviewers = databaseHelper.getTrustedReviewers("testStudent");
        assertTrue(trustedreviewers.contains("testReviewer"));
    }
    
    /**
     * Test 4: Remove from trusted reviewers list 
     */
    @Test
    public void testdeleteTrustedReviewer() throws SQLException {
        databaseHelper.addTrustedReviewer("testStudent", "testReviewer");
        boolean removed = databaseHelper.deleteTrustedReviewer("testStudent", "testReviewer");
        assertTrue(removed);
        assertFalse(databaseHelper.getTrustedReviewers("testStudent").contains("testReviewer"));
    }
    
    /**
     * Test 5: Cannot add duplicates 
     */
    @Test 
    public void testcannotAddReviewerTwice() throws SQLException {
        boolean firstAdd = databaseHelper.addTrustedReviewer("testStudent", "testReviewer");
        assertTrue(firstAdd, "First add succeeds");

        boolean secondAdd = databaseHelper.addTrustedReviewer("testStudent", "testReviewer");
        assertFalse(secondAdd, "Second add fails");

        List<String> trusted = databaseHelper.getTrustedReviewers("testStudent");
        long count = trusted.stream().filter(r -> r.equals("testReviewer")).count();

        assertEquals(1, count, "reviewer appear once");
    }
    /**
     * Test 6: Handels empty reviewer list
     */
    public void testEmptyTrustedReviewerList() throws SQLException{
    	List<String> trustedreviewers = databaseHelper.getTrustedReviewers("testStudent");
    	assertNotNull(trustedreviewers, "List should not be null");
        assertTrue(trustedreviewers.isEmpty(),"list empty when no trusted reviewers");
    }

}