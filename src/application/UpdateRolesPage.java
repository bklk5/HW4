package application;
import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
* UpdateRolesPage class represents the page where an admin can add or remove roles for a user.
*/
public class UpdateRolesPage {
	public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
		/**
		 * Displays the Update Roles Page in the provided primary stage.
	     *
	     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
	     * @param primaryStage   The primary stage where the scene will be displayed.
	     */
   	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	   
	    // label to display the Update User Roles
	    Label roleLabel = new Label("Update User Roles");
	    roleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    roleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
      
       // Label to inform the user what information is needed
       Label enterUsernameLabel = new Label("Enter the username whose role you want to modify:");
      
       // Text field to enter username
       TextField usernameField = new TextField();
       usernameField.setPromptText("Username");
      
       // add/remove dropdown
       ChoiceBox<String> actionChoice = new ChoiceBox<>();
       actionChoice.getItems().addAll("Add", "Remove");
       actionChoice.setValue("Add");
      
       // role selection drop down
       ChoiceBox<String> roleChoice = new ChoiceBox<>();
       roleChoice.getItems().addAll("adminRole", "studentRole", "instructorRole", "staffRole", "reviewerRole");
      
       //  update the role button
       Button updateRoleButton = new Button("Update Role");
      
       // Back Button
       Button backButton = new Button("<--");
      
       // display the update status
       Label updateStatusLabel = new Label("");
       updateStatusLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
      
       // display an error message if any
       Label updateErrorLabel = new Label("");
       updateErrorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
      
       // back to the main page
       backButton.setOnAction(a -> {
           new AdminHomePage(databaseHelper).show(primaryStage);
       });
      
       // Does role update
       updateRoleButton.setOnAction(a -> {
           String username = usernameField.getText().trim();
           String action = actionChoice.getValue().toLowerCase();
           String role = roleChoice.getValue();
          
           if (username.isEmpty() || role == null) {
               updateErrorLabel.setText("Error: Username and role must be provided.");
               updateStatusLabel.setText("");
               return;
           }
           
           boolean isUserAdmin = databaseHelper.isUserAdmin(username);

           if (isUserAdmin && action.equals("add") && role.equals("adminRole")) {
               updateErrorLabel.setText("Error: An admin cannot change another admin's role.");
               updateStatusLabel.setText("");
               return;
           }
           if (isUserAdmin && action.equals("remove") && role.equals("adminRole")) {
               updateErrorLabel.setText("Error: An admin cannot change another admin's role.");
               updateStatusLabel.setText("");
               return;
           }
           boolean success = databaseHelper.add_remove_Role(username, action, role);
           if (success) {
               updateStatusLabel.setText("Role " + (action.equals("add") ? "added to" : "removed from") + " " + username + " successfully!");
               updateErrorLabel.setText("");
           } else {
               updateErrorLabel.setText("Error: Operation failed.");
               updateStatusLabel.setText("");
           }
       });
      
       layout.getChildren().addAll(roleLabel, enterUsernameLabel, usernameField, actionChoice, roleChoice, updateRoleButton, updateStatusLabel, updateErrorLabel, backButton);
       Scene updateRolesScene = new Scene(layout, 800, 400);
      
       // Set the scene to primary stage
       primaryStage.setScene(updateRolesScene);
       primaryStage.setTitle("Update Roles Page");
   }
}
