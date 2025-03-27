package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;



public class passwordUi {
	
	
	public static void updateFlags(Label upperCaseLabel, Label lowerCaseLabel, Label numericDigitLabel, Label specialCharLabel, Label longEnoughLabel, Label generalErrorLabel, String input) {
		
		// If textfield empty, return password empty
		if(input == "") {
			generalErrorLabel.setText("*** ERROR *** Password is Empty");
			generalErrorLabel.setTextFill(Color.RED);
			return;
		}
		
		// Check every password requirements
		// If satisfied, change the color to green and update the error messages
		
		if (PasswordEvaluator.foundUpperCase) {
			upperCaseLabel.setText("At least one upper case letter - Satisfied");
			upperCaseLabel.setTextFill(Color.GREEN);
		}

		if (PasswordEvaluator.foundLowerCase) {
			lowerCaseLabel.setText("At least one lower case letter - Satisfied");
			lowerCaseLabel.setTextFill(Color.GREEN);
		}

		if (PasswordEvaluator.foundNumericDigit) {
			numericDigitLabel.setText("At least one numeric digit - Satisfied");
			numericDigitLabel.setTextFill(Color.GREEN);
		}

		if (PasswordEvaluator.foundSpecialChar) {
			specialCharLabel.setText("At least one special character - Satisfied");
			specialCharLabel.setTextFill(Color.GREEN);
		}

		if (PasswordEvaluator.foundLongEnough) {
			longEnoughLabel.setText("At least eight characters - Satisfied");
			longEnoughLabel.setTextFill(Color.GREEN);
		}
		// Make the general error message to Invalid, if an unknow character is inputed
		// Change the general error message specifying that there is an unknow character
		if((!PasswordEvaluator.foundLongEnough || !PasswordEvaluator.foundSpecialChar ||  !PasswordEvaluator.foundNumericDigit || !PasswordEvaluator.foundLowerCase || !PasswordEvaluator.foundUpperCase) && !PasswordEvaluator.otherChar) {
			 generalErrorLabel.setText("Invalid Password");
			 generalErrorLabel.setTextFill(Color.RED);
			}
		else {
			 generalErrorLabel.setText("Only alphabetical, numerical and ~`!@#$%^&*()_-+{}[]|:,.?/ characters are allowed");
			 generalErrorLabel.setTextFill(Color.RED);}
		
		// Check if the password is correct,
		// If it is change the generalErrorLabel to correct password
		if (PasswordEvaluator.foundUpperCase && PasswordEvaluator.foundLowerCase &&
				PasswordEvaluator.foundNumericDigit && PasswordEvaluator.foundSpecialChar &&PasswordEvaluator.foundLongEnough && !PasswordEvaluator.otherChar) {
					generalErrorLabel.setText("Valid Password");
					generalErrorLabel.setTextFill(Color.GREEN);
				}
	}
	
	public static void resetAssessments(Label upperCaseLabel, Label lowerCaseLabel, Label numericDigitLabel, Label specialCharLabel, Label longEnoughLabel, Label generalErrorLabel ) {
		
		// Reset all of the error messages to it original state
		
		upperCaseLabel.setText("At least one upper case letter - Not yet satisfied");
	  
		upperCaseLabel.setTextFill(Color.RED);
	    
		lowerCaseLabel.setText("At least one lower case letter - Not yet satisfied");
		lowerCaseLabel.setTextFill(Color.RED);
	    
		numericDigitLabel.setText("At least one numeric digit - Not yet satisfied");
		numericDigitLabel.setTextFill(Color.RED);
	    
		specialCharLabel.setText("At least one special character - Not yet satisfied");
		specialCharLabel.setTextFill(Color.RED);
	    
		longEnoughLabel.setText("At least eight characters - Not yet satisfied");
		longEnoughLabel.setTextFill(Color.RED);
		generalErrorLabel.setText("");
		generalErrorLabel.setTextFill(Color.RED);
	}
	
	

}
