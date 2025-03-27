package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectRolePage {
	
	private final DatabaseHelper databaseHelper;
	
	public SelectRolePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	
	   public void show(Stage primaryStage, User user) {
	    	VBox layout = new VBox();
		    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		    
		    // label to display the title of the page
		    Label selectRoleLabel = new Label("Select Role");
		    
		    // Style the label
		    selectRoleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		    
		    // Add the label to the layout 
		    layout.getChildren().add(selectRoleLabel);
		    
		    // Make a button appear to go to a specific role home page only if the user has the following role
		    if (user.isAdmin()) {
		    	Button adminButton = new Button("Admin");
		    	// If button is pressed, will send the user to the admin page
		    	adminButton.setOnAction(a -> {
		    		new AdminHomePage(databaseHelper).show(primaryStage);
	            });
		    	// Add the button to the layout
	            layout.getChildren().add(adminButton);
	        }
		    
		    if (user.isStudent()) {
		    	Button studentButton = new Button("Student");
		    	// If button is pressed, will send the user to the student page
		    	studentButton.setOnAction(a -> {
		    		new StudentHomePage(databaseHelper).show(primaryStage,user);
	            });
		    	// Add the button to the layout
	            layout.getChildren().add(studentButton);
	        }
		    
		    if (user.isInstructor()) {
		    	Button instructorButton = new Button("Instructor");
		    	// If button is pressed, will send the user to the instructor page
		    	instructorButton.setOnAction(a -> {
		    		new InstructorHomePage(databaseHelper).show(primaryStage);
	            });
		    	// Add the button to the layout
	            layout.getChildren().add(instructorButton);
	        }
		    
		    if (user.isStaff()) {
		    	Button staffButton = new Button("Staff");
		    	// If button is pressed, will send the user to the staff page
		    	staffButton.setOnAction(a -> {
		    		new StaffHomePage(databaseHelper).show(primaryStage);
	            });
		    	// Add the button to the layout
	            layout.getChildren().add(staffButton);
	        }
		    
		    if (user.isReviewer()) {
		    	Button reviewerButton = new Button("Reviewer");
		    	// If button is pressed, will send the user to the reviewer page
		    	reviewerButton.setOnAction(a -> {
		    		new ReviewerHomePage(databaseHelper).show(primaryStage);
	            });
		    	// Add the button to the layout
	            layout.getChildren().add(reviewerButton);
	        }


		    // Create the scene 
		    Scene rolesScene = new Scene(layout, 800, 400);

		    // Set the scene to primary stage
		    primaryStage.setScene(rolesScene);
		    primaryStage.setTitle("Select role");
	    }

}
