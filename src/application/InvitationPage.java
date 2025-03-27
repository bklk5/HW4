package application;


import databasePart1.*;

import java.sql.*;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InvitePage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button.
 */

public class InvitationPage {

	/**
     * Displays the Invite Page in the provided primary stage.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper,Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display the title of the page
	    Label userLabel = new Label("Invite ");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    //Label to inform the user what information is needed to enter
	    Label selectRoleLabel = new Label("Select the role of the new User");
	    
	    // Button to generate the invitation code
	    Button showCodeButton = new Button("Generate Invitation Code");
	    
	    Button backButton = new Button("<--");
	    
	    // Display the role options buttons
        CheckBox isAdminButton = new CheckBox("Admin");
        CheckBox isStudentButton = new CheckBox("Student");
        CheckBox isInstructorButton = new CheckBox("Instructor");
        CheckBox isStaffButton = new CheckBox("Staff");
        CheckBox isReviewerButton = new CheckBox("Reviewer");
        
        
	    
        
	    // Label to display the generated invitation code
	    Label inviteCodeLabel = new Label(""); 
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        // Label in case there is an error
        Label inviteCodeError = new Label("");
        inviteCodeError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        // Go back to the main page
        backButton.setOnAction(a ->{
        	new AdminHomePage(databaseHelper).show(primaryStage);
        	
        });
        
 
        showCodeButton.setOnAction(a -> {
        	
        	//Reset Role option
        	boolean adminRole = false;
        	boolean studentRole = false;
        	boolean instructorRole = false;
        	boolean staffRole = false;
        	boolean reviewerRole = false;
        	
        	if(isAdminButton.isSelected())
        		adminRole= true;
        	if(isStudentButton.isSelected())
        		studentRole= true;
        	if(isInstructorButton.isSelected())
        		instructorRole = true;
        	if(isStaffButton.isSelected())
        		staffRole = true;
        	if(isReviewerButton.isSelected())
        		reviewerRole = true;
        	
        	if(adminRole || studentRole || instructorRole || staffRole || reviewerRole) {
        		
        	
        	// Add the time it was made
        	Timestamp timeInviteMade = new Timestamp(System.currentTimeMillis());
        	
        	// Generate the invitation code using the databaseHelper and set it to the label
        	  String invitationCode = databaseHelper.generateInvitationCode(adminRole,studentRole,instructorRole,staffRole,reviewerRole, timeInviteMade);
        	  inviteCodeLabel.setText(invitationCode);
        	  inviteCodeError.setText("");
        	 
        	}
        	else {
        		
        		inviteCodeError.setText("Must select at least one role");
        		
        	}
        });
	    

        layout.getChildren().addAll(userLabel,selectRoleLabel, isAdminButton,isStudentButton,isInstructorButton,isStaffButton,isReviewerButton,showCodeButton, inviteCodeLabel, inviteCodeError,backButton);
	    Scene inviteScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(inviteScene);
	    primaryStage.setTitle("Invite Page");
    	
    }
}