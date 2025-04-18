package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class IndividualReviewPage {
	
    private final DatabaseHelper databaseHelper;

    public IndividualReviewPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user, Question question, Review review) throws SQLException {
    	try {
            databaseHelper.connectToDatabase(); // Connect to the database
            if (databaseHelper.isDatabaseEmpty()) {
            	new FirstPage(databaseHelper).show(primaryStage);
            } else {
            	databaseHelper.printQuestions();
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    	
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
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        System.out.println("Content: " + review.getContent());
        System.out.println("Author: " + review.getAuthor());
        
        Question q = new Question("", "", "", "");
		
		try {
			q = databaseHelper.readQuestionById(question.getId());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Label title = new Label(q.getTitle());
        Label author = new Label("By : " + q.getAuthor());
        Label header = new Label("Your Review: ");
        Button updateButton = new Button("Update review");
        Button deleteButton = new Button("Delete review");
//        Button messageButton = new Button("Send message");
		Label contentText = new Label(review.getContent());

		
		updateButton.setOnAction(a -> {
			new UpdateReviewPage(databaseHelper).show(primaryStage, user, question, review);
		});
		
		deleteButton.setOnAction(a -> {
			
			// Add a warming that this action can not be undone
			  Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
	    	            "Are you sure you want to delete this review?\n This action can not be undone!" , ButtonType.YES, ButtonType.NO);
	    	        confirmationAlert.setTitle("Confirm Deletion");

	    	        //check the response
	    	        confirmationAlert.showAndWait().ifPresent(response -> {
	    	            if (response == ButtonType.YES) {
	    	                // Proceed with deletion if user confirms

	    	            	databaseHelper.deleteQuestionReview(review.getId());
	    	    			new ReviewsList(databaseHelper).show(primaryStage, user);
	    	    			
	    	                }
	    	            });
				
		});
		
//		messageButton.setOnAction(a ->{
//			System.out.println("sending message to " + review.getAuthor());
//			new CreateMessage(databaseHelper).showQuestionReviewer(primaryStage, user, question, review);
//		});
		
		// - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        
        if (user.getUserName().equals(review.getAuthor()) || user.isReviewer()) {
        	buttonContainer.getChildren().addAll(updateButton, deleteButton);
        }
        
        // - - - - - - - - - - - - - - - RATING - - - - - - - - - - - - - - 
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        double averageRating = databaseHelper.getAverageRatingsForAllReviews()
                .getOrDefault(review.getId(), 0.0);
        Label averageRatingLabel = new Label("Average Rating: " + String.format("%.1f", averageRating));
        averageRatingLabel.setStyle("-fx-text-fill: darkgreen;");

        // STUDENT RATING SECTION
        Label rateLabel = new Label("Rate this review (1–10):");
        ComboBox<Integer> ratingCombo = new ComboBox<>();
        ratingCombo.getItems().addAll(java.util.stream.IntStream.rangeClosed(1, 10).boxed().toList());

        Button submitRating = new Button("Submit Rating");
        submitRating.setOnAction(e -> {
            Integer rating = ratingCombo.getValue();
            if (rating == null) {
                showAlert("Please select a rating before submitting.");
                return;
            }

            try {
                databaseHelper.addOrUpdateReviewRating(review.getId(), user.getUserName(), rating);
                showAlert("Rating submitted!");
                new ReviewsList(databaseHelper).show(primaryStage, user);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error submitting rating.");
            }
        });
        
        VBox centerContent = new VBox(10, buttonContainer, title, author, header, contentText, averageRatingLabel, rateLabel, ratingCombo, submitRating);
        centerContent.setStyle("-fx-padding: 20px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);      // Add navigation bar
        borderPane.setCenter(centerContent); // Main content area

        // Set the Scene and Show
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Forums");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showAnswerReview(Stage primaryStage, User user, Answer answer, Review review) throws SQLException {
    	try {
            databaseHelper.connectToDatabase(); // Connect to the database
            if (databaseHelper.isDatabaseEmpty()) {
            	new FirstPage(databaseHelper).show(primaryStage);
            } else {
            	databaseHelper.printQuestions();
            }
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    	
    	// - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	// Set up buttons for top nav bar 
    	Button homeButton = new Button("Home");
    	Button forumsButton = new Button("Forums");
    	Button searchButton = new Button("Search");
    	Button reviewersListButton = new Button("Trusted Reviewers List");
    	Button messagesButton = new Button("Messages");
    	Button logoutButton = new Button("Logout");
    	Button reviewsListButton = new Button("Reviews");
    	
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

    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar();
        
        if(user.isReviewer()) {
        	rightContainer.setPrefWidth(310);
        	toolbar.getItems().addAll(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton, reviewsListButton, rightContainer);
        }
        else {
        	rightContainer.setPrefWidth(380);
        	toolbar.getItems().addAll(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton, rightContainer);
        }
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        System.out.println("Content: " + review.getContent());
        System.out.println("Author: " + review.getAuthor());
        
        Answer ans = new Answer(0, "", "");
		
		try {
			ans = databaseHelper.readAnswerById(answer.getId());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Label title = new Label(ans.getContent());
        Label author = new Label("By : " + ans.getAuthor());
        Label header = new Label("Your Review: ");
        Button updateButton = new Button("Update review");
        Button deleteButton = new Button("Delete review");
//        Button messageButton = new Button("Send message");
		Label contentText = new Label(review.getContent());

		
		updateButton.setOnAction(a -> {
			System.out.println("updating");
			new UpdateReviewPage(databaseHelper).showAnswerReview(primaryStage, user, answer, review);
		});
		
		deleteButton.setOnAction(a -> {
			
			// Add a warming that this action can not be undone
			  Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
	    	            "Are you sure you want to delete this review?\n This action can not be undone!" , ButtonType.YES, ButtonType.NO);
	    	        confirmationAlert.setTitle("Confirm Deletion");

	    	        //check the response
	    	        confirmationAlert.showAndWait().ifPresent(response -> {
	    	            if (response == ButtonType.YES) {
	    	                // Proceed with deletion if user confirms

	    	            	databaseHelper.deleteAnswerReview(review.getId());
	    	    			new ReviewsList(databaseHelper).show(primaryStage, user);
	    	    			
	    	                }
	    	            });
				
		});
		
//		messageButton.setOnAction(a ->{
//			System.out.println("sending message to " + review.getAuthor());
//			new CreateMessage(databaseHelper).showAnswerReviewer(primaryStage, user, answer, review);
//		});
		
		// - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        
        if (user.getUserName().equals(review.getAuthor()) || user.isReviewer()) {
        	buttonContainer.getChildren().addAll(updateButton, deleteButton);
        }
        
        // - - - - - - - - - - - - - - - RATING - - - - - - - - - - - - - - 
        
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        
        double averageRating = databaseHelper.getAverageRatingsForAllReviews()
                .getOrDefault(review.getId(), 0.0);
        Label averageRatingLabel = new Label("Average Rating: " + String.format("%.1f", averageRating));
        averageRatingLabel.setStyle("-fx-text-fill: darkgreen;");

        // STUDENT RATING SECTION
        Label rateLabel = new Label("Rate this review (1–10):");
        ComboBox<Integer> ratingCombo = new ComboBox<>();
        ratingCombo.getItems().addAll(java.util.stream.IntStream.rangeClosed(1, 10).boxed().toList());

        Button submitRating = new Button("Submit Rating");
        submitRating.setOnAction(e -> {
            Integer rating = ratingCombo.getValue();
            if (rating == null) {
                showAlert("Please select a rating before submitting.");
                return;
            }

            try {
                databaseHelper.addOrUpdateAnswerReviewRating(review.getId(), user.getUserName(), rating);
                showAlert("Rating submitted!");
                new ReviewsList(databaseHelper).show(primaryStage, user);
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error submitting rating.");
            }
        });
        
        VBox centerContent = new VBox(10, buttonContainer, title, author, header, contentText, averageRatingLabel, rateLabel, ratingCombo, submitRating);
        centerContent.setStyle("-fx-padding: 20px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolbar);      // Add navigation bar
        borderPane.setCenter(centerContent); // Main content area

        // Set the Scene and Show
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("Forums");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}