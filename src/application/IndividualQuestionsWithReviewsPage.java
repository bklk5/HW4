package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class IndividualQuestionsWithReviewsPage {
	
    private final DatabaseHelper databaseHelper;

    public IndividualQuestionsWithReviewsPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user, Question question) throws SQLException {
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
        Button updateButton = new Button("Update Question");
        Button deleteButton = new Button("Delete Question");
        Label questionText = new Label(question.getTitle());
		Label authorText = new Label(question.getAuthor());
		Label contentText = new Label(question.getContent());
		Button messageButton = new Button("Send Message");
		Button reviewButton = new Button("Review Question");
		Button questionsButton = new Button("Questions");
		
		questionsButton.setOnAction(a -> {
			try {
				new IndividualQuestionPage(databaseHelper).show(primaryStage, user, question);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		});
		
		updateButton.setOnAction(a -> {
			new UpdateQuestionPage(databaseHelper).show(primaryStage, user, question);
		});
		
		deleteButton.setOnAction(a -> {
			
			// Add a warming that this action can not be undone
			  Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
	    	            "Are you sure you want to delete this question?\n This action can not be undone!" , ButtonType.YES, ButtonType.NO);
	    	        confirmationAlert.setTitle("Confirm Deletion");

	    	        //check the response
	    	        confirmationAlert.showAndWait().ifPresent(response -> {
	    	            if (response == ButtonType.YES) {
	    	                // Proceed with deletion if user confirms

	    	    			databaseHelper.deleteQuestion(question.getId());
	    	    			new Forums(databaseHelper).show(primaryStage, user);
	    	                
	    	                }
	    	            });
	    	
		});
		
		messageButton.setOnAction(a -> {
			System.out.println("sending message to " + question.getAuthor());
			new CreateMessage(databaseHelper).show(primaryStage, user, question);
		});
		
		reviewButton.setOnAction(a -> {
			new CreateQuestionReview(databaseHelper).show(primaryStage, user, question);
		});
		
		ObservableList<QuestionReview> items = FXCollections.observableArrayList();
		ListView<QuestionReview> listView = new ListView<>(items);

		try {
		    databaseHelper.connectToDatabase(); // Connect to the database

		    if (databaseHelper.isDatabaseEmpty()) {
		        new FirstPage(databaseHelper).show(primaryStage);
		        return; // Exit early if database is empty
		    } else {
		        // Get reviews directly from the database and add to the observable list
		        List<QuestionReview> reviews = databaseHelper.getQuestionReviewsByQuestionId(question.getId());
		        items.addAll(reviews);
		    }
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
    	
        
    	
    	// Handle button for listview upon clicking on individual list element 
        listView.setOnMouseClicked(a -> {
        	if (a.getClickCount() >= 2) {
                Review selectedItem = listView.getSelectionModel().getSelectedItem();
                
                if(selectedItem != null) {
                	try {
                		// take person to page of question
						new IndividualReviewPage(databaseHelper).show(primaryStage, user, question, selectedItem);
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
        	}
        });
        
        listView.setCellFactory(param -> new ListCell<QuestionReview>() {
            @Override
            protected void updateItem(QuestionReview review, boolean empty) {
                super.updateItem(review, empty);
                
                if (empty || review == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a custom layout for each cell
                    Label contentLabel = new Label(review.getAuthor() + " said: " + review.getContent());
                    contentLabel.setWrapText(true); // Enable text wrapping
                    
                    VBox cellContent = new VBox(10,contentLabel);
                    setGraphic(cellContent);
                }
            }
        });

		// - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        
       

        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - -
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        
        if (user.getUserName().equals(question.getAuthor()) || user.isReviewer()) {
        	buttonContainer.getChildren().addAll(updateButton, deleteButton,questionsButton);
        }
        
        VBox centerContent = new VBox(10, buttonContainer, authorText, questionText, contentText, reviewButton, messageButton, listView);
        centerContent.setStyle("-fx-padding: 20px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);      // Add navigation bar
        borderPane.setCenter(centerContent); // Main content area

        // Set the Scene and Show
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Forums");
        primaryStage.setScene(scene);
        primaryStage.show();
        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - - 

    }
}