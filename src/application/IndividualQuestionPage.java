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
public class IndividualQuestionPage {
	
    private final DatabaseHelper databaseHelper;

    public IndividualQuestionPage(DatabaseHelper databaseHelper) {
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
    	
    	// container to right align logout button
    	HBox rightContainer = new HBox(logoutButton);
    	rightContainer.setPrefWidth(380);
    	rightContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
    	
    	homeButton.setOnAction(a -> new HomePage(databaseHelper).show(primaryStage, user));
    	forumsButton.setOnAction(a -> new Forums(databaseHelper).show(primaryStage, user));
    	searchButton.setOnAction(e -> new SearchQuestions(databaseHelper).show(primaryStage, user));
    	// set on action with reviewersListButton
    	messagesButton.setOnAction(a -> new MessagesPage(databaseHelper).show(primaryStage,user));
        logoutButton.setOnAction(a -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));

    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton,rightContainer);
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
        

        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        Button updateButton = new Button("Update Question");
        Button deleteButton = new Button("Delete Question");
        Label questionText = new Label(question.getTitle());
		Label authorText = new Label(question.getAuthor());
		Label contentText = new Label(question.getContent());
		Button messageButton = new Button("Send Message");
		Button answerButton = new Button("Answer Question");
		
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
		
		answerButton.setOnAction(a -> {
			new CreateAnswer(databaseHelper).show(primaryStage, user, question);
		});
		
		ObservableList<Answer> items = FXCollections.observableArrayList();
    	ListView<Answer> listView = new ListView<>(items);
    	
    	AnswersList aList = new AnswersList();
    	
        try {
            databaseHelper.connectToDatabase(); // Connect to the database

            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
                return; // Exit early if database is empty
            } else {
            	
            	// Add answer titles to listview 
            	aList.setAnswers(databaseHelper.readAnswersByQuestionId(question.getId()));
                items.addAll(aList.getAnswers()); 

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	
//    	System.out.println(question.getId());
//    	aList.setAnswers(databaseHelper.readAnswersByQuestionId(question.getId()));
//    	items.addAll(aList.getAnswers());
        
        // Set custom cell factory to display questions in a readable way
        listView.setCellFactory(param -> new javafx.scene.control.ListCell<Answer>() {
            @Override
            protected void updateItem(Answer a, boolean empty) {
                super.updateItem(a, empty);
                
        		ToggleButton upvoteButton = new ToggleButton("⇧"); 
        		Label voteCount = new Label(); 
        		
        		VBox voteBox = new VBox(10, upvoteButton, voteCount); 
        		
                upvoteButton.setOnAction(b -> {
        			databaseHelper.incrementUpvote(a.getId());
        			int votes = databaseHelper.getUpvote(a.getId());
        			a.setUpvotes(votes+1);
        			voteCount.setText(String.valueOf(a.getUpvotes()));
        			//update the gui to reflect the new changes 
        			try {
						items.setAll(databaseHelper.readAnswersByQuestionId(question.getId()));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
        			
        		});
                
                
                
                if (empty || a == null) {
                    setText(null);
                } else {
                	voteCount.setText(String.valueOf(databaseHelper.getUpvote(a.getId())));
                    setText("Answer: " + a.getContent());
                    HBox voteSpacing = new HBox(10, voteBox);
                    setGraphic(voteSpacing);
                    
                }
            }
        });
    	
    	// Handle button for listview upon clicking on individual list element 
        listView.setOnMouseClicked(a -> {
        	if (a.getClickCount() >= 2) {
                Answer selectedItem = listView.getSelectionModel().getSelectedItem();
                
                if(selectedItem != null) {
                	try {
                		// take person to page of question
						new IndividualAnswerPage(databaseHelper).show(primaryStage, user, question, selectedItem);
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
        	}
        });
		// - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        
       

        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - - 
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        
        if (user.getUserName().equals(question.getAuthor())) {
        	buttonContainer.getChildren().addAll(updateButton, deleteButton);
        }
        
        VBox centerContent = new VBox(10, buttonContainer, authorText, questionText, contentText, answerButton, messageButton, listView);
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