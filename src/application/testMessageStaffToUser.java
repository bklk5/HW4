package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databasePart1.*;

class testMessageStaffToUser {
	private DatabaseHelper databaseHelper;
	private List<Message> message;
	private Answer answer;
	private User staff;
	private Question question;
	private Review review;
	private Review different_review;
	
	@BeforeEach 
	void setUp() {
		databaseHelper = new DatabaseHelper();
		message = new ArrayList<>();
		answer = new Answer(1, "author", "content");
		staff = new User("staff", "passwordStudent", false, false, false, true, false);
		question = new Question("question title", "question content", "question author", "question category");
		review = new QuestionReview(1, "review author", "review content");
		different_review = new QuestionReview(2, "different review author", "different review content");
	}
	
	/*
	 * Test 1: Verify the student is the sender
	 */
	@Test
	public void successfulMessage() {
		String content = "Hi i have a question about your review";
		Message message = new Message(staff.getUserName(), review.getAuthor(), content);
		
		assertEquals(staff.getUserName(), message.getSender());
	}
	
	/*
	 * Test 2: Verify that the answer is flagged
	 */
	@Test 
	public void successfulFlagged() {
		answer.setFlagged(true);
		assertEquals(answer.isFlagged(), true);
	}
	
	/*
	 * Test 3: Verify that flagging does not affect answer's content
	 */
	@Test
	public void preservedContent() {
		answer.setFlagged(true);
		assertEquals("content", answer.getContent());
	}
	
}