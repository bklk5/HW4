package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button loginButton = new Button("Login");
        
        // Back button to go back to the previous page
        Button backButton = new Button("<--");
     // Go back to the setup/login Selection page 
        backButton.setOnAction(a ->{
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        	
        });
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            
            
            try {
            	// Reset the user role
            	boolean adminRole = false;
            	boolean studentRole = false;
            	boolean instructorRole = false;
            	boolean staffRole = false;
            	boolean reviewerRole = false; 
            	
            	
            	// Create a new user with the given password and username
            	// The user currently has no role assigned
            	User user=new User(userName, password, adminRole, studentRole,instructorRole,staffRole, reviewerRole);
            	
            	// Retrieve the user's role from the database using userName
            	adminRole = databaseHelper.isUserAdmin(userName);
            	studentRole = databaseHelper.isUserStudent(userName);
            	instructorRole = databaseHelper.isUserInstructor(userName);
            	staffRole = databaseHelper.isUserStaff(userName);
            	reviewerRole = databaseHelper.isUserReviewer(userName);
            	//Update the current user with the roll fetch from the database
            	user.setAdminRole(adminRole);
            	user.setStudentRole(studentRole);
            	user.setInstructorRole(instructorRole);
            	user.setStaffRole(staffRole);
            	user.setReviewerRole(reviewerRole);
            	
            	int totalRole = databaseHelper.numberOfRoles(userName);
            	//Check if the user has at least one role
            	if(totalRole > 0) {
            		
            		// check if the credential are correct
            		if(databaseHelper.login(user)) {
            		//check if the user has more then one role
            		if(totalRole > 1) {
            			new SelectRolePage(databaseHelper).show(primaryStage,user);
            		}
            		// Only one role assigned, go directly to the user role home page
            		else {
            			if (adminRole) {
            				new HomePage(databaseHelper).show(primaryStage, user);
            				
            			}
            			else if (studentRole) {
            				new StudentHomePage(databaseHelper).show(primaryStage, user);
            				new HomePage(databaseHelper).show(primaryStage, user);
            			}
            			else if (instructorRole) {
            				new InstructorHomePage(databaseHelper).show(primaryStage);
            				
            			}
            			else if (staffRole) {
            				new StaffHomePage(databaseHelper).show(primaryStage);
            			}
            			else if (reviewerRole) {
            				new ReviewerHomePage(databaseHelper).show(primaryStage);
            				}
            			
            		}
            		
            		
            		}
            		
            
            		else {
            			// Display an error if the login fails
            			// Need more detail error for password and username
            			// implement here
                        errorLabel.setText("Error logging in");
            		}
            	}
            	else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
            
           
            
        });

	// Button when user forgot password
   	Button forgotPasswordButton = new Button("Forgot Password");
   	forgotPasswordButton.setOnAction(a -> {
		new OnetimePasswordPage().show(databaseHelper, primaryStage);
	});

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel,forgotPasswordButton,backButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
