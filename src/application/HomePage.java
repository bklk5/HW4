package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class HomePage {
	
	private final DatabaseHelper databaseHelper;

    public HomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage, User user) {
    	// - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	// Set up buttons for top nav bar 
    	Button homeButton = new Button("Home");
    	Button forumsButton = new Button("Forums");
    	Button searchButton = new Button("Search");
    	
    	Button reviewersListButton = new Button("Trusted Reviewers List");
    	Button messagesButton = new Button("Messages");
    	Button logoutButton = new Button("Logout");
    	Button reviewsListButton = new Button("Reviews");
    	Button reviewerRequest  = new Button("Reviewer Requests");

    	// container to right align logout button
    	HBox rightContainer = new HBox(logoutButton);
    	rightContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
    	
    	homeButton.setOnAction(a -> new HomePage(databaseHelper).show(primaryStage, user));
    	forumsButton.setOnAction(a -> new Forums(databaseHelper).show(primaryStage, user));
    	searchButton.setOnAction(e -> new SearchQuestions(databaseHelper).show(primaryStage, user));
    	// set on action with reviewersListButton
    	reviewersListButton.setOnAction(a -> new TrustedReviewersPage(databaseHelper).show(primaryStage,user));
    	messagesButton.setOnAction(a -> new MessagesPage(databaseHelper).show(primaryStage,user));
        logoutButton.setOnAction(a -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));
        reviewsListButton.setOnAction(a -> new ReviewsList(databaseHelper).show(primaryStage, user));
        reviewerRequest.setOnAction(a -> new displayStudentsRequestForReviewerRole(databaseHelper).show(primaryStage, user));
    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar();
        
        if(user.isCurrentRoleReviewer()) {
        	rightContainer.setPrefWidth(310);
        	toolbar.getItems().addAll(homeButton, forumsButton,messagesButton, searchButton, reviewsListButton, rightContainer);
        }
        else if(user.isCurrentRoleInstructor()) {
        	rightContainer.setPrefWidth(260);
        	toolbar.getItems().addAll(homeButton, forumsButton,messagesButton, searchButton, reviewerRequest, rightContainer);
        }
        else if(user.isCurrentRoleStudent()) {
        	rightContainer.setPrefWidth(260);
        	toolbar.getItems().addAll(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton, rightContainer);
        }
        else {
        	rightContainer.setPrefWidth(380);
        	toolbar.getItems().addAll(homeButton, forumsButton,messagesButton, searchButton, rightContainer);
        }
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
        
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        Label welcomeText = new Label("Welcome " + user.getUserName() + "!");
	    Button inviteButton = new Button("Invite");
	    Button oneTimePasswordButton = new Button("One Time Password");
	    Button listUsersButton = new Button("List Users");
	    Button removeUsersButton = new Button("Remove Users");
	    Button updateRoleButton = new Button("Update Role");
	    Button requestReviewerRoleButton = new Button("Request Reviewer Access");
	    
	    // styling 
	    welcomeText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	   
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
        // Student request to become a reviewer
        requestReviewerRoleButton.setOnAction(a -> {
        	databaseHelper.add_remove_Role(user.getUserName(), "add", "requestReviewerRole");
        	// alert student the request was made
	        Alert requestAlert = new Alert(Alert.AlertType.INFORMATION);
	        requestAlert.setTitle("Reviewer Role request");
	        requestAlert.setContentText("A request to become a reviewer was send to the instructor! ");
	        requestAlert.setHeaderText("");
	        requestAlert.showAndWait();
	        
	        requestReviewerRoleButton.setVisible(false);
        });

    	
    	if (user.isCurrentRoleAdmin()) {
    		System.out.println("USER IS ADMIN");
    	}
    	else {
    		System.out.println("USER IS NOT ADMIN");
    	}
    	
    	// set on action with reviewersButton
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    // - - - - - - - - - - - - - - - CONTENT END  - - - - - - - - - - - - - - 
	    
	    
	    
        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - -
        VBox centerContent = new VBox(10, welcomeText);
        
        // conditionally render options for user depending on their role
        if (user.isCurrentRoleAdmin()) {
        	centerContent.getChildren().addAll(inviteButton,oneTimePasswordButton,listUsersButton,removeUsersButton,updateRoleButton);
        }
        if(user.isCurrentRoleStudent() && !user.isReviewer() && !databaseHelper.isUserRequestingtoBecomeReviewer(user.getUserName())) {
        	centerContent.getChildren().add(requestReviewerRoleButton);
        }
        
        
        centerContent.setStyle("-fx-padding: 20px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);      // Add navigation bar
        borderPane.setCenter(centerContent); // Main content area

        // Set the Scene and Show
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Forums");
        primaryStage.setScene(scene);
        primaryStage.show();
        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES END - - - - - - - - - - - - - - 
    }
}