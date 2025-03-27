package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.*;
/**
* The SetupAdmin class handles the setup process for creating an administrator account.
* This is intended to be used by the first user to initialize the system with admin credentials.
*/
public class AdminSetupPage {
	
   private final DatabaseHelper databaseHelper;
  
   public AdminSetupPage(DatabaseHelper databaseHelper) {
       this.databaseHelper = databaseHelper;
     
      
   }
   public void show(Stage primaryStage) {
   	// Input fields for name, email, userName and password
   	 TextField nameField = new TextField();
        nameField.setPromptText("Enter Admin Name");
        nameField.setMaxWidth(250);
       
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Admin Email");
        emailField.setMaxWidth(250);
       TextField userNameField = new TextField();
       userNameField.setPromptText("Enter Admin userName");
       userNameField.setMaxWidth(250);
       PasswordField passwordField = new PasswordField();
       passwordField.setPromptText("Enter Password");
       passwordField.setMaxWidth(250);
       Button setupButton = new Button("Setup");
      
      
       // Label to display error messages for name, email, username and password
       Label errorNameLabel = new Label();
       errorNameLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
       Label errorEmailLabel = new Label();
       errorEmailLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
       Label errorUserNameLabel = new Label();
       errorUserNameLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
       Label errorPasswordLabel = new Label();
       errorPasswordLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
      
      
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
      
       setupButton.setOnAction(a -> {
       	// Retrieve user input
       	String name = nameField.getText();
       	String email = emailField.getText();
          String userName = userNameField.getText();
          String password = passwordField.getText();
          
           // Verify user input and password
           String errorNameMessage = NameRecognizer.checkForValidName(name);
           String errorEmailMessage = EmailRecognizer.checkForValidEmail(email);
           String errorUserNameMessage = UserNameRecognizer.checkForValidUserName(userName);
           String errorPasswordMessage = PasswordEvaluator.evaluatePassword(password);
          
         
          
           if( errorUserNameMessage == "" && errorPasswordMessage == "" && errorNameMessage == "" && errorEmailMessage=="") {
           	
           	// All requirements met, proceed to creating new user
    
           try {
           	// Create a new User object with only the admin role and no other roles
           	// Register in the database
           	boolean adminRole = true;
           	boolean studentRole = false;
           	boolean instructorRole = false;
           	boolean staffRole = false;
           	boolean reviewerRole = false;
           	
           	
           	User user=new User(name, email, userName, password, adminRole, studentRole,instructorRole, staffRole,reviewerRole);
               databaseHelper.register(user);
               System.out.println("Administrator setup completed.");
              
               // Navigate to the Login Page
               new UserLoginPage(databaseHelper).show(primaryStage);
           } catch (SQLException e) {
               System.err.println("Database error: " + e.getMessage());
               e.printStackTrace();
           }
           }
          
           else {
           	// Display an error for the name, email, username or password that does not meet requirements
           	
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
          
       });
       // Set up the layout
       VBox layout = new VBox(10, nameField,errorNameLabel, emailField,errorEmailLabel, userNameField, errorUserNameLabel,passwordField,errorPasswordLabel,setupButton);
       layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
       // Add the layout to the scene
       primaryStage.setScene(new Scene(layout, 800, 400));
       primaryStage.setTitle("Administrator Setup");
       primaryStage.show();
   }
}
