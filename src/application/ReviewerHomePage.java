package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerHomePage {
	
	private final DatabaseHelper databaseHelper;
	
	 public ReviewerHomePage(DatabaseHelper databaseHelper) {
	        this.databaseHelper = databaseHelper;
	    }
	
	
	 public void show(Stage primaryStage) {
	    	VBox layout = new VBox();
	    	
		    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		    Button logoutButton = new Button("Logout");
		    
		    // label to display the welcome message for the admin
		    Label reviewerLabel = new Label("Hello, Reviewer!");
		    
		    reviewerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		    layout.getChildren().addAll(reviewerLabel,logoutButton);
		    
		 // Logout user
	        logoutButton.setOnAction(a -> {
	        	System.out.println("logging out...");
	        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	        });
		    
		    
		    Scene reviewerScene = new Scene(layout, 800, 400);

		    // Set the scene to primary stage
		    primaryStage.setScene(reviewerScene);
		    primaryStage.setTitle("Reviewer Page");
	    }

}
