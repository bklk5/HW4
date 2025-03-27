package application;

import java.sql.Timestamp;

public class Message {
	private int id;
	private int questionId;
	private String sender;
	private String receiver;
	private Timestamp timestamp;
	private String content;
	
	// CREATE METHOD (constructor)
	public Message(int id, String sender, String receiver, String content, Timestamp timestamp) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public Message(int questionId, String sender, String receiver, String content) {
		this.questionId = questionId;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}
	
	public Message(String sender, String receiver, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}
	
	// GETTER METHODS 
	public int getId() {
		return this.id;
	}
	
	public int getQuestionId() {
		return this.questionId;
	}

	public String getContent() {
		return this.content;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public Timestamp getTimestamp() {
		return this.timestamp;
	}
	
	public String getReceiver() {
		return this.receiver;
	}
	
	// SETTER METHODS 
	public void setId(int id) {
		this.id = id;
	}
	
	public void setQuestionId(int id) {
		this.questionId = id;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setTime(Timestamp timestamp) {
		this.timestamp = timestamp;
	}	
}
