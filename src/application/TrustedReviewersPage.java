package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;


/**
 * This page displays the list of trusted reviewers for each page
 */

public class TrustedReviewersPage {
	
	private final DatabaseHelper databaseHelper;

    public TrustedReviewersPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	/**
     * Displays the trusted reviewers page in the provided primary stage.
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
        	toolbar.getItems().addAll(homeButton, forumsButton,reviewersListButton,messagesButton, searchButton, rightContainer);
        }
        else {
        	rightContainer.setPrefWidth(380);
        	toolbar.getItems().addAll(homeButton, forumsButton,messagesButton, searchButton, rightContainer);
        }
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
        
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        Label welcomeText = new Label("Welcome Student,  " + user.getUserName() + "!");
        Button addButton = new Button("Add reviewer");
        Button removeButton = new Button("Remove reviewer");
        Button trustedSearch = new Button("Search trusted reviewers reviews");
	    
	    // styling 
	    welcomeText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	   
	    // Set up listview to show list of trusted reviewers 
    	ObservableList<String> items = FXCollections.observableArrayList();
    	ListView<String> listView = new ListView<>(items);

        try {
            databaseHelper.connectToDatabase(); // Connect to the database

            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
                return; // Exit early if database is empty
            } else {
            	List<String> trustedReviewers = databaseHelper.getTrustedReviewers(user.getUserName());
                items.addAll(trustedReviewers);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        // Set custom cell factory to display questions in a readable way
        listView.setCellFactory(param -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String rn, boolean empty) {
                super.updateItem(rn, empty);
                if (empty || rn == null) {
                    setText(null);
                } else {
                    setText("Reviewer Name: " + rn);
                }
            }
        });
        
	    // dropdown for adding trusted reviewer
        ComboBox <String> reviewerComboBox = new ComboBox<>();
        ObservableList<String> items2 = FXCollections.observableArrayList();
        
        try {
        	List<String> allReviewers = databaseHelper.getAllReviewers();
        	
        	if (allReviewers.isEmpty()) {
                System.out.println("no reviewers found");
            } else {
                System.out.println("reviewers: " + allReviewers);  // Debugging
            }
        	
        	items2.addAll(allReviewers);
        	reviewerComboBox.setItems(items2);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	    // Add reviewer 
        addButton.setOnAction(a -> {
        	
        	String selectedReviewer = reviewerComboBox.getValue();
        	
        	if (selectedReviewer != null) {
                boolean added = databaseHelper.addTrustedReviewer(user.getUserName(), selectedReviewer);
                if (added) {
                        if (!items.contains(selectedReviewer)) { 
                            items.add(selectedReviewer);
                        }
                    reviewerComboBox.setValue(null);
                } else {
                    System.out.println("not added.");
                }
            } else {
                System.out.println("select reviewer");
            }
        });

	    // Remove reviewer 
        removeButton.setOnAction(a -> {
        	String selectedReviewer = listView.getSelectionModel().getSelectedItem();
            if (selectedReviewer != null) {
                boolean removed = databaseHelper.deleteTrustedReviewer(user.getUserName(), selectedReviewer);
                if (removed) {
                    items.remove(selectedReviewer);
                } else {
                    System.out.println("not removed.");
                }
            } else {
                System.out.println("select reviewer.");
            }
        });

    	
    	if (user.isCurrentRoleStudent()) {
    		System.out.println("USER IS STUDENT");
    	}
    	else {
    		System.out.println("USER IS NOT STUDENT");
    	}
    	// Search by trusted reviewer
    	trustedSearch.setOnAction(a -> new trustedSearchPage(databaseHelper).show(primaryStage, user));
    	
    	
    	// set on action with reviewersButton
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    // - - - - - - - - - - - - - - - CONTENT END  - - - - - - - - - - - - - - 
	    
	    
	    
        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - -
        VBox centerContent = new VBox(10, welcomeText);
        centerContent.getChildren().addAll(reviewerComboBox, addButton,removeButton,trustedSearch, listView);
        
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