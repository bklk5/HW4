package application;

import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * OnetimePasswordPage class represents the page where the user can receive a onetime password
 * The onetime password is displayed upon clicking a button.
 */

public class OnetimePasswordPage {
	public void show (DatabaseHelper databaseHelper, Stage primaryStage) {
		VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //primaryStage.show();
	    
	    // Buttons
	    Button confirmButton = new Button("Confirm");		
	    Button backButton = new Button("<--");
	    
	    Label passwordError = new Label("");
	    passwordError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
	    
	    // textfield to type in the onetime password
	    TextField input = new TextField();
	    input.setPromptText("Enter the one-time password");
	    input.setMaxWidth(250);
	    
	    TextField username_textfield = new TextField();
	    username_textfield.setPromptText("Enter your username");
	    username_textfield.setMaxWidth(250);
	    
	    
	    // confirming onetime password after "Confirm"
	    confirmButton.setOnAction(a -> {
	    	// if onetime password valid then go to new page
	    	String writtenPassword = input.getText();
	    	String username = username_textfield.getText();
	    	boolean password_valid = databaseHelper.validateOnetimePassword(writtenPassword, username);
	    	// verify password 
	    	if (password_valid) {
	    		new NewPasswordSetup(databaseHelper).show(primaryStage, username);	// go to new password setup page 
	    	}
	    	else {
	    		passwordError.setText("Incorrect one-time password input");
	    	}
	    });
	    
	 // Go back to the main page
        backButton.setOnAction(a ->{
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        	
        });
	    
	    layout.getChildren().addAll(username_textfield,input, passwordError, confirmButton, backButton);
	    primaryStage.setScene(new Scene(layout, 800, 400));		// add layout to the scene
	    primaryStage.setTitle("One-time Password Confirmation");
	}
}
