package application;

import javafx.scene.paint.Color;
import javafx.scene.control.Label;

public class usernameUi {
	
	
	
	public static void  updateFlags(Label errorUserNameLabel, String error) {
		
		// If there is an error, set the text to the error
		// Change the color of the text to red
		errorUserNameLabel.setText(error);
   		errorUserNameLabel.setTextFill(Color.RED);
   		// Check if there is no error
   		if(error == "") {
		   	// There is no error the username is valid
   			// Change the error to Valid Username 
   			// Change the color to green
        		errorUserNameLabel.setText("Valid Username");
        		errorUserNameLabel.setTextFill(Color.GREEN);
		   	} 
		
	}

}
