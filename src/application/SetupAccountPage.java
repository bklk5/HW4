package application;

import javafx.scene.Scene;
import java.sql.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their name, email, userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for name,email, userName, password, and invitation code
    	TextField nameField = new TextField();
        nameField.setPromptText("Enter Name");
        nameField.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setMaxWidth(250);
        
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        Button setupButton = new Button("Setup");
        
        // Label to display error messages for name, email, username and password
        Label errorGeneralLabel = new Label();
        errorGeneralLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        Label errorNameLabel = new Label();
        errorNameLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        Label errorEmailLabel = new Label();
        errorEmailLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        Label errorUserNameLabel = new Label();
        errorUserNameLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        Label errorPasswordLabel = new Label();
        errorPasswordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        // Button to go back to the previous page
        Button backButton = new Button("<--");
        
        
        // Listener to update password textfield as input changes
        passwordField.textProperty().addListener((observable, oldValue, newValue) 
				-> { 
					
					// Retrieve most recent password 
					String password = passwordField.getText();
					 // Check if there is any password error
					String errorPasswordMessage = PasswordEvaluator.evaluatePassword(password);
					// Update password message label
					errorPasswordLabel.setText(errorPasswordMessage);
					 
				});
      
       // Listener to update username textfield as input changes
       userNameField.textProperty().addListener((observable, oldValue, newValue)
    		   -> { 
    			   // Retrieve most recent username
    			   String userName = userNameField.getText();
    			   	// Check if there is any username error
    			   String errorUserNameMessage = UserNameRecognizer.checkForValidUserName(userName);
    			   // Update the username error message
    			   errorUserNameLabel.setText(errorUserNameMessage);
		});
    // Listener to update name textfield as input changes 
       nameField.textProperty().addListener((observable, oldValue, newValue)
    		   -> { 
    			   // Retrieve most recent name
    			   String name = nameField.getText();
    			   	// Check if there is any name error
    			   String errorNameMessage = NameRecognizer.checkForValidName(name);
    			   // Update the name error message
    			   errorNameLabel.setText(errorNameMessage);
		});
       // Listener to update email textfield as input changes 
       emailField.textProperty().addListener((observable, oldValue, newValue)
    		   -> { 
    			   // Retrieve most recent email
    			   String email = emailField.getText();
    			   	// Check if there is any email error
    			   String errorEmailMessage = EmailRecognizer.checkForValidEmail(email);
    			   // Update the email error message
    			   errorEmailLabel.setText(errorEmailMessage);
		});

       
    // Go back to the setup/login Selection page 
       backButton.setOnAction(a ->{
       	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
       	
       });
       
       
        

        setupButton.setOnAction(a -> {
        	
        	
        	
        	// Retrieve user input
        	String name = nameField.getText();
        	String email = emailField.getText();
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            
            // verify user input and password
            String errorNameMessage = NameRecognizer.checkForValidName(name);
            String errorEmailMessage = EmailRecognizer.checkForValidEmail(email);
            String errorUserNameMessage = UserNameRecognizer.checkForValidUserName(userName);
            String errorPasswordMessage = PasswordEvaluator.evaluatePassword(password);
          
            	
            try {
            	
            	
            	// Check if the name, email, password and username are valid
            	
            	if(errorUserNameMessage == "" && errorPasswordMessage == "" && errorNameMessage == "" && errorEmailMessage== "") {
            	// name, email, username and password valid
            		
            	// Check if the user already exists
            	if(!databaseHelper.doesUserExist(userName)) {
            		
            		// Validate the invitation code
            		
            		
            		//If the invitation code is valid, proceed
            		if(databaseHelper.validateInvitationCode(code)) {
            			
            			// Invitation code is valid, now it a question of has it expired ?
            			Timestamp timeOfCodeUsed = new Timestamp(System.currentTimeMillis());
                		Timestamp timeInviteMade = databaseHelper.getTimeInviteMade(code);
            			long timDifference = timeOfCodeUsed.getTime() - timeInviteMade.getTime();
                		
                		// the time is given in milliseconds, therefore one day is equivalent to 86400000
                		// If  it has been less then a day since the invitation code was made, then the code is validated and a user can be created
                		if(timDifference < 86400000 ) {
            			
            			// Create a new user and register them in the database
            			
            			boolean adminRole = true;
                    	boolean studentRole = false;
                    	boolean instructorRole = false;
                    	boolean staffRole = false;
                    	boolean reviewerRole = false;
                    	//get the role from the database based on the admin decision
                    	boolean roles[] = databaseHelper.initialRoles(code);
                    	adminRole = roles[0];
                    	studentRole = roles[1];
                    	instructorRole = roles[2];
                    	staffRole = roles[3];
                    	reviewerRole  = roles[4];
                    	
                    	
		            	User user=new User(name, email, userName, password,adminRole,studentRole,instructorRole,staffRole,reviewerRole);
		                databaseHelper.register(user);
		                
		             // Navigate to the new user specific role page based on it assigned role or roles
		             
		            	int totalRole = databaseHelper.numberOfRoles(userName);
		               
		      
		            	//check if the user has more then one role
		            	if(totalRole > 1) {
		            			new SelectRolePage(databaseHelper).show(primaryStage,user);
		            	}
		            		// Only one role assigned, go directly to the user role home page
		            	else {
		            		if (adminRole) {
		            			new AdminHomePage(databaseHelper).show(primaryStage);
		            				
		            		}
		            		else if (studentRole) {
		            			new StudentHomePage(databaseHelper).show(primaryStage,user);
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
                			errorGeneralLabel.setText("The code has expired, it been longer than a day since code was send");
                		}
		                
            		
            		}
            			else {
            			
            			// Display an error for the invitation code
            			errorGeneralLabel.setText("Please enter a valid invitation code");
            		
            		}
            		
            	}
            	else {
            		errorGeneralLabel.setText("This name is taken!!.. Please use another to setup an account");
            	}
            	
            	}
            	// The name, email, password or the username does not meet the requirement
            	else {
            		// Check if name is valid or not 
                	errorNameMessage = NameRecognizer.checkForValidName(name);
                	// Update error message 
                	errorNameLabel.setText(errorNameMessage);
                	// Check if email is valid or not 
                	errorEmailMessage = EmailRecognizer.checkForValidEmail(email);
                	// Update error message 
                	errorEmailLabel.setText(errorEmailMessage);
 
            		// Check if the username is valid or not
                	errorUserNameMessage = UserNameRecognizer.checkForValidUserName(userName);
                	// Update error message 
                	errorUserNameLabel.setText(errorUserNameMessage);
                	// Check if the password is valid or not
                	errorPasswordMessage = PasswordEvaluator.evaluatePassword(password);
                	// Update error message 
                	errorPasswordLabel.setText(errorPasswordMessage);
            		
            	}
        
        
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
            
           
         
        });
        // Display the layout on the scene
        VBox layout = new VBox(3);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(nameField, errorNameLabel, emailField, errorEmailLabel, userNameField,errorUserNameLabel,passwordField,errorPasswordLabel,inviteCodeField, errorGeneralLabel,setupButton,backButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}

