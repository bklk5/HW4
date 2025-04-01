package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;

class StudentRoleRequestTest {
	
	private DatabaseHelper databaseHelper;
	
	
	@BeforeEach 
	void setUp() throws SQLException {
		databaseHelper = new DatabaseHelper();
		databaseHelper.connectToDatabase();
		
		databaseHelper.register(new User("Charly","password",false,true,false,false,false));
		
	}
	@AfterEach
	void cleanDatabase() {	
		databaseHelper.deleteUser("Charly");
	}
	
	/**
	 * Test 1: Verifies that request to become a reviewer from a student is successfully store in database
	 */
	@Test
	void testStudentRequest() {
		
		databaseHelper.add_remove_Role("Charly", "add", "requestReviewerRole");
		assertEquals(databaseHelper.isUserRequestingtoBecomeReviewer("Charly"), true);
	}
	
	/**
	 * Test 2: Verifies that when a student request to become a reviewer, the student appear in the list of interested student from the perspective of the instructor
	 */
	@Test
	void requestingStudentsAppearsToInstructor() {
		databaseHelper.add_remove_Role("Charly", "add", "requestReviewerRole");
		List<User> students = databaseHelper.retrieveStudentsReviewerRequest();
		Boolean find = false;
		for(int i = 0; i < students.size(); i++) {
			if(students.get(i).getUserName().equals("Charly")) 
				find = true;
			
		}
		assertEquals(find, true);
	}
	/**
	 * Test 3: Verifies that when an instructor accept a student request to become a reviewer, the student has the role reviewer enable
	 */
	@Test
	void testInstructorAccept() {
		
		databaseHelper.add_remove_Role("Charly", "add", "requestReviewerRole");
		databaseHelper.add_remove_Role("Charly","add", "reviewerRole");
		databaseHelper.add_remove_Role("Charly", "remove", "requestReviewerRole");
		assertEquals(databaseHelper.isUserReviewer("Charly"), true);
	}
	
	/**
	 * Test 4: Verifies that when an instructor decline a student request to become a reviewer, the student does not have the role reviewer enable
	 */
	
	@Test
	void testInstructorDecline() {
		
		databaseHelper.add_remove_Role("Charly", "add", "requestReviewerRole");
		databaseHelper.add_remove_Role("Charly", "remove", "requestReviewerRole");
	
		assertEquals(databaseHelper.isUserReviewer("Charly"),false);
	}
	/**
	 * Test 5: Verifies that when an instructor decline a student request to become a reviewer, the student is no longer
	 * in the list of student interested of becoming a reviewer
	 */
	
	@Test
	void testInstructorList1() {
		
		databaseHelper.add_remove_Role("Charly", "add", "requestReviewerRole");
		databaseHelper.add_remove_Role("Charly", "remove", "requestReviewerRole");
		List<User> students = databaseHelper.retrieveStudentsReviewerRequest();
		Boolean find = false;
		for(int i = 0; i < students.size(); i++) {
			if(students.get(i).getUserName().equals("Charly")) 
				find = true;
			
		}
		assertEquals(find, false);
	}
	/**
	 * Test 6: Verifies that when an instructor accept a student request to become a reviewer, the student is no longer
	 * in the list of student interested of becoming a reviewer
	 */
	
	@Test
	void testInstructorList2() {
		
		databaseHelper.add_remove_Role("Charly", "add", "requestReviewerRole");
		databaseHelper.add_remove_Role("Charly","add", "reviewerRole");
		databaseHelper.add_remove_Role("Charly", "remove", "requestReviewerRole");
		List<User> students = databaseHelper.retrieveStudentsReviewerRequest();
		Boolean find = false;
		for(int i = 0; i < students.size(); i++) {
			if(students.get(i).getUserName().equals("Charly")) 
				find = true;
			
		}
		assertEquals(find, false);
	}
	
	

}
