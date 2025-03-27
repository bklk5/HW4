package application;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
* DeleteUsersPage class represents the page where an admin can add or remove roles for a user.
*/
public class DeleteUserPage {
	public void show(DatabaseHelper databaseHelper, Stage PrimaryStage) {
		/**
		 * Displays the Update Roles Page in the provided primary stage.
	     *
	     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
	     * @param primaryStage   The primary stage where the scene will be displayed.
	     */
		VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	   
	    // Label to display the title of the page
	    Label userLabel = new Label("Delete User");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	   
	 // Label to inform the user what information is needed
       Label enterUsernameLabel = new Label("Enter the username of the user to delete:");
      
       // Text field to enter username
       TextField usernameField = new TextField();
       usernameField.setPromptText("Username");
      
       // delete the user button
       Button deleteUserButton = new Button("Delete User");
      
       // button back
       Button backButton = new Button("<--");
      
       // display the deletion status
       Label deletionStatusLabel = new Label("");
       deletionStatusLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
      
       // display an error message if any
       Label deletionErrorLabel = new Label("");
       deletionErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
      
       // back to the main page
       backButton.setOnAction(a -> {
           new AdminHomePage(databaseHelper).show(PrimaryStage);
       });
      
       // does user deletion
       deleteUserButton.setOnAction(a -> {
    	   String username = usernameField.getText().trim();
    	   if (!username.isEmpty()) {
    	        // Ask yes or no for the deletion
    	        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
    	            "Are you sure you want to delete the user " + username + "?", ButtonType.YES, ButtonType.NO);
    	        confirmationAlert.setTitle("Confirm Deletion");

    	        //check the response
    	        confirmationAlert.showAndWait().ifPresent(response -> {
    	            if (response == ButtonType.YES) {
    	                // Proceed with deletion if user confirms
    	                boolean success = databaseHelper.deleteUser(username);
    	                if (success) {
    	                    deletionStatusLabel.setText("User successfully deleted.");
    	                    deletionErrorLabel.setText("");
    	                } else {
    	                    deletionErrorLabel.setText("Error: User not found or could not be deleted.");
    	                    deletionStatusLabel.setText("");
    	                }
    	            }
    	        });
    	    } else {
    	        deletionErrorLabel.setText("Error: Username field cannot be empty.");
    	        deletionStatusLabel.setText("");
    	    }
    	});
       
      
       layout.getChildren().addAll(userLabel, enterUsernameLabel, usernameField, deleteUserButton, deletionStatusLabel, deletionErrorLabel, backButton);
       Scene deleteUserScene = new Scene(layout, 800, 400);
      
       // Set the scene to primary stage
       PrimaryStage.setScene(deleteUserScene);
       PrimaryStage.setTitle("Delete User Page");
   }
}
