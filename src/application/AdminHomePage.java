package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	private final DatabaseHelper databaseHelper;

    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    // Button for admin features
	    Button inviteButton = new Button("Invite");
	    Button oneTimePasswordButton = new Button("One Time Password");
	    Button listUsersButton = new Button("List Users");
	    Button removeUsersButton = new Button("Remove Users");
	    Button updateRoleButton = new Button("Update Role");
	    Button logoutButton = new Button("Logout");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	   
	    // Go to the invite page
        inviteButton.setOnAction(a -> {
            new InvitationPage().show(databaseHelper, primaryStage);
        });
	    // Go to the one time password page
        oneTimePasswordButton.setOnAction(a -> {
            //page and feature need implementation
        	new AdminOnetimePassword().show(databaseHelper, primaryStage);
        	
        });
        // Go to the list of user page
        listUsersButton.setOnAction(a -> {
        	new AdminUserListPage(databaseHelper).show(primaryStage);
        	//page and feature need implementation
        });
        // Go to remove users page
        removeUsersButton.setOnAction(a -> {
        	//page and feature need implementation
        	new DeleteUserPage().show(databaseHelper, primaryStage);
        });
        // Go to update role page
        updateRoleButton.setOnAction(a -> {
        	//page and feature need implementation
        	new UpdateRolesPage().show(databaseHelper, primaryStage);
        });
        // Logout user
        logoutButton.setOnAction(a -> {
        	System.out.println("logging out...");
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
	    

	    
        
	    // Add all of the button and label to the scene 
	    layout.getChildren().addAll(adminLabel,inviteButton,oneTimePasswordButton,listUsersButton,removeUsersButton,updateRoleButton, logoutButton);
	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
}