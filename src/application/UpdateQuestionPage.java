package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class UpdateQuestionPage {
	
    private final DatabaseHelper databaseHelper;

    public UpdateQuestionPage(DatabaseHelper databaseHelper) {
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
    	
    	// - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	// Set up buttons for top nav bar 
    	Button homeButton = new Button("Home");
    	Button forumsButton = new Button("Forums");
    	
    	homeButton.setOnAction(a -> new HomePage(databaseHelper).show(primaryStage, user));
    	forumsButton.setOnAction(a -> new Forums(databaseHelper).show(primaryStage, user));
    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar(homeButton, forumsButton);
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        TextField titleField = new TextField(question.getTitle()); 
        titleField.setPromptText("Enter title");

        TextField contentField = new TextField(question.getContent());
        contentField.setPromptText("Enter content");
        contentField.setPrefHeight(100);

        TextField categoryField = new TextField(question.getCategory());
        categoryField.setPromptText("Enter Category");

        Button updateButton = new Button("Update Question");
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
		
		updateButton.setOnAction(a -> {
			try {
				// retrieve user input
				String title = titleField.getText();
	        	String content = contentField.getText();
	        	String category = categoryField.getText();
				if(QuestionRecognizer.checkQuestion(title, content, category).equals("")) {
					boolean update = databaseHelper.updateQuestion(question.getId(), title, content, category);
	                
					if (update) {
						new Forums(databaseHelper).show(primaryStage, user);
					}
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
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        
        VBox centerContent = new VBox(10, titleField, contentField, categoryField, updateButton, errorLabel);
        centerContent.setStyle("-fx-padding: 20px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);      // Add navigation bar
        borderPane.setCenter(centerContent); // Main content area

        // Set the Scene and Show
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Forums");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}