package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

public class CreateQuestion {
	private final DatabaseHelper databaseHelper;

    public CreateQuestion(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
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
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - -
    	// Input fields for userName and password
    	Label questionLabel = new Label("Hello " + user.getUserName());
    	
        TextField titleField = new TextField();
        titleField.setPromptText("Enter title");

        TextField contentField = new TextField();
        contentField.setPromptText("Enter content");
        contentField.setPrefHeight(100);

        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter Category");

        Button setupButton = new Button("create question");
        
        Button  backButton = new  Button("<--");
        
        
        // Go back to the forum page
        backButton.setOnAction(a ->{
        	new Forums(databaseHelper).show(primaryStage, user);
        });
        
        
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
        	String title = titleField.getText();
        	String content = contentField.getText();
        	String author = user.getUserName();
        	String category = categoryField.getText();
        	
            try {
            	// check validity of inputs
            	if(QuestionRecognizer.checkQuestion(title,content,category).equals("")) {
            		// Create a new Question object with parameters and insert into table
                	Question question = new Question(title, content, author, category);
                    databaseHelper.createQuestion(question);
                    new Forums(databaseHelper).show(primaryStage, user);
            	}
            	else {
            		errorLabel.setText(QuestionRecognizer.checkQuestion(title,content,category));
            	}
                
                
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - -


        VBox layout = new VBox(10, questionLabel, titleField, contentField, categoryField, setupButton, errorLabel, backButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
