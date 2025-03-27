package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentHomePage {
	

	private final DatabaseHelper databaseHelper;

    public StudentHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	
	
	 public void show(Stage primaryStage, User user) {
	    	VBox layout = new VBox();
	    	
		    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		    
		    // label to display the welcome message for the admin
		    Label studentLabel = new Label("Hello, Student!");
		    Button createQuestionButton = new Button ("Create Question");
		    Button homePageButton = new Button("Go to Forums");
		    Button logoutButton = new Button("Logout");
		    
		    studentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		    layout.getChildren().addAll(studentLabel,createQuestionButton,homePageButton,logoutButton);
		    Scene studentScene = new Scene(layout, 800, 400);

		 // Logout user
	        logoutButton.setOnAction(a -> {
	        	System.out.println("logging out...");
	        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	        });
	        
	        // Navigate to create question page
	        createQuestionButton.setOnAction(a -> {
	        	new CreateQuestion(databaseHelper).show(primaryStage, user);
	        });
	        
	        // Navigate to homepage
	        homePageButton.setOnAction(a -> {
	        	new Forums(databaseHelper).show(primaryStage, user);
	        });
		    
		    
		    // Set the scene to primary stage
		    primaryStage.setScene(studentScene);
		    primaryStage.setTitle("Student Page");
	    }
}
