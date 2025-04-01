package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databasePart1.*;

class TestMessageToReviewer {
	private DatabaseHelper databaseHelper;
	private List<Message> message;
	private User student;
	private Question question;
	private Review review;
	private Review different_review;
	
	@BeforeEach 
	void setUp() {
		databaseHelper = new DatabaseHelper();
		message = new ArrayList<>();
		student = new User("student", "passwordStudent", false, true, false, false, false);
		question = new Question("question title", "question content", "question author", "question category");
		review = new Review(1, "review author", "review content");
		different_review = new Review(2, "different review author", "different review content");
	}
	
	/**
	 * Test 1: Verify the student is the sender
	 */
	@Test
	void testStudentSender() {
		String content = "Hi i have a question about your review";
		Message message = new Message(student.getUserName(), review.getAuthor(), content);
		
		assertEquals(student.getUserName(), message.getSender());
	}
	
	/**
	 * Test 2: Verify the reviewer is the receiver
	 */
	@Test 
	void testReviewerReceiver() {
		String content = "Hi i have a question about your review";
		Message message = new Message(student.getUserName(), review.getAuthor(), content);
		
		assertEquals(review.getAuthor(), message.getReceiver());
	}
	
	/**
	 * Test 3: Verify empty content is allowed but probably shouldn't be 
	 */
	@Test
	void testEmptyMessage() {
		String content = "";
		Message message = new Message(student.getUserName(), review.getAuthor(), content);
		
		// verify content is empty 
		assertEquals(content, message.getContent());
	}
	
	/**
	 * Test 4: Verify long content is allowed but probably shouldn't
	 */
	@Test
	void testLongMessage() {
		String content = "A".repeat(200);
		Message message = new Message(student.getUserName(), review.getAuthor(), content);
		assertEquals(200, message.getContent().length());
	}
	
	/**
	 * Test 5: Verify that the message is sent to the correct reviewer (receiver)
	 */
	@Test 
	void testCorrectReceiver() {
		Message message = new Message(student.getUserName(), review.getAuthor(), "Hi reviewer 1");
		Message different_message = new Message(student.getEmail(), different_review.getAuthor(), "Hi the other reviewer");
		
		assertNotEquals(message.getReceiver(), different_message.getReceiver());
	}
	
	/**
	 * Test 6: Verify that the student can send multiple messages to the same reviewer
	 */
	@Test
	void testMultipleMessage() {
		Message message1 = new Message(student.getUserName(), review.getAuthor(), "message 1");
		Message message2 = new Message(student.getUserName(), review.getAuthor(), "message 2");
		Message message3 = new Message(student.getUserName(), review.getAuthor(), "message 3");
		
		message.add(message3);
		message.add(message2);
		message.add(message1);
		
		assertEquals(3, message.size());
	}
}
