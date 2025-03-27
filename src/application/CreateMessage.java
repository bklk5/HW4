package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

public class CreateMessage {
	private final DatabaseHelper databaseHelper;

    public CreateMessage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user, Question question) {
    	// Input fields for userName and password
    	Label messageLabel = new Label("Sending message to " + question.getAuthor());

        TextField contentField = new TextField();
        contentField.setPromptText("Enter content");
        contentField.setPrefHeight(100);

        Button setupButton = new Button("Send Message");
        Button  backButton = new  Button("<--");
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        //Go back to the quesiton page
        backButton.setOnAction(a ->{
        	try {
				new IndividualQuestionPage(databaseHelper).show(primaryStage, user, question);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
        	String content = contentField.getText();
        	String sender = user.getUserName();
        	String receiver = question.getAuthor();
        	
        	// connect to database
        	try {
                databaseHelper.connectToDatabase(); 
                if (databaseHelper.isDatabaseEmpty()) {
                	new FirstPage(databaseHelper).show(primaryStage);
                } else {
                	// create message and insert into database
                	Message m = new Message(sender, receiver, content);
                    databaseHelper.sendMessage(m);
                    new Forums(databaseHelper).show(primaryStage, user);
                }
            } catch (SQLException e) {
            	System.out.println(e.getMessage());
            }
        });

        VBox layout = new VBox(10, messageLabel, contentField, setupButton, errorLabel, backButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
