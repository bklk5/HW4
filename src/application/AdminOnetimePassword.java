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

public class AdminOnetimePassword {
	public void show (DatabaseHelper databaseHelper, Stage primaryStage) {
		VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //primaryStage.show();
	    
	 // Username textfield
	    TextField username_textfield = new TextField();
	    username_textfield.setPromptText("Write your username");
	    username_textfield.setMaxWidth(250);
	    
	    // Buttons
	    Button showPasswordButton = new Button("Generate One-time Password");
	    Button backButton = new Button("<--");
	    
	    // Label to display the generated onetime password 
	    Label passwordLabel = new Label(""); 
	    passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
	    Label generalError = new Label("");
	    generalError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
	    
	   
	    
	    // showing the onetime password after "Generate One-time Password" if the username exists
	    showPasswordButton.setOnAction(a -> {
	    	String username = username_textfield.getText(); 
	    	if (databaseHelper.doesUserExist(username)) {
	    		
	    		String password = databaseHelper.generateOnetimePassword(username);
		    	passwordLabel.setText(password);
		    	generalError.setText("");
	    	}
	    	else {
	    		generalError.setText("Username does not exist");
	    	}
	    });
	    
	 // Go back to the main page
        backButton.setOnAction(a ->{
        	new AdminHomePage(databaseHelper).show(primaryStage);
        	
        });
	    
	    
	    
	    layout.getChildren().addAll(username_textfield,showPasswordButton, passwordLabel, generalError,backButton);
	    primaryStage.setScene(new Scene(layout, 800, 400));		// add layout to the scene
	    primaryStage.setTitle("One-time Password Confirmation");
	}
}