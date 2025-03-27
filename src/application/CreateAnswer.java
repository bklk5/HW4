package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

public class CreateAnswer {
	private final DatabaseHelper databaseHelper;

    public CreateAnswer(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user, Question question) {
    	try {
            databaseHelper.connectToDatabase(); // Connect to the database
            if (databaseHelper.isDatabaseEmpty()) {
            	new FirstPage(databaseHelper).show(primaryStage);
            } else {
            	databaseHelper.printQuestions();
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
        
    	// Input fields for userName and password
    	Label questionLabel = new Label("Answering question : " + question.getTitle());

        TextField contentField = new TextField();
        contentField.setPromptText("Enter content");
        contentField.setPrefHeight(100);

        Button setupButton = new Button("create answer");
        Button  backButton = new  Button("<--");
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        
        
        //Go back to the question page
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
        	String author = user.getUserName();
        	
            try {
            	
            	if(AnswerRecognizer.checkAnswer(content).equals("")) {
                	// Create a new Question object with parameters and insert into table
                	Answer answer = new Answer(question.getId(), author, content);
                    databaseHelper.createAnswer(answer);
                    
                    new IndividualQuestionPage(databaseHelper).show(primaryStage, user, question);
            	}
            	else {
            		errorLabel.setText(AnswerRecognizer.checkAnswer(content));
            	}
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, questionLabel, contentField, setupButton, errorLabel,backButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
