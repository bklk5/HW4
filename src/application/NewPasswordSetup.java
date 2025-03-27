package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import databasePart1.*;
public class NewPasswordSetup {
	private DatabaseHelper databaseHelper;
	private Connection connection = null;
	
	public NewPasswordSetup (DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	public void show (Stage primaryStage, String username) {
		VBox layout = new VBox();
		
		layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
		
		// Input field for the new password
		TextField new_password = new TextField();
		new_password.setPromptText("Enter the new password");
		new_password.setMaxHeight(250);
		
		Button acceptButton = new Button("Accept");
		
		// Label to display error messages for invalid password
		Label errorPasswordLabel = new Label();
		errorPasswordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
		
		// Listener to update password as input changes
		new_password.textProperty().addListener((observable, oldValue, newValue)
				-> {
					// retrieve the most recent password
					String recent_password = new_password.getText();
					// error messages
					String error_message = PasswordEvaluator.evaluatePassword(recent_password);
					// update error messages
					errorPasswordLabel.setText(error_message);
				});
		
		acceptButton.setOnAction(a -> {
			// retrieve password input
			String password = new_password.getText();
			
			// verify password
			String errorMessage = PasswordEvaluator.evaluatePassword(password);
			
			
			if (errorMessage == "") {
				
					databaseHelper.changePassword(password, username);
					new UserLoginPage(databaseHelper).show(primaryStage);
				
			}
		});
		// Set up the layout
       //VBox layout = new VBox(10, new_password,errorPasswordLabel,acceptButton);
       //layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
       // Add the layout to the scene
	
	   layout.getChildren().addAll(new_password,acceptButton, errorPasswordLabel);
       primaryStage.setScene(new Scene(layout, 800, 400));
       primaryStage.setTitle("New Password Setup");
       primaryStage.show();
	}
}
