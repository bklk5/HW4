package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffHomePage {
	
	private final DatabaseHelper databaseHelper;

    public StaffHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	
	
	  public void show(Stage primaryStage) {
	    	VBox layout = new VBox();
	    	
		    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		    
		    // label to display the welcome message for the admin
		    Label staffLabel = new Label("Hello, Staff!");
		    Button logoutButton = new Button("Logout");
		    
		    staffLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		    layout.getChildren().addAll(staffLabel,logoutButton);
		    Scene staffScene = new Scene(layout, 800, 400);
		    
		    
		    // Logout user
	        logoutButton.setOnAction(a -> {
	        	System.out.println("logging out...");
	        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	        });
		    
		    

		    // Set the scene to primary stage
		    primaryStage.setScene(staffScene);
		    primaryStage.setTitle("Staff Page");
	    }

}
