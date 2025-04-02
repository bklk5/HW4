package application;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewsList {
	private final DatabaseHelper databaseHelper;

    public ReviewsList(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

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
    	messagesButton.setOnAction(a -> new MessagesPage(databaseHelper).show(primaryStage,user));
        logoutButton.setOnAction(a -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));
        reviewsListButton.setOnAction(a -> new ReviewsList(databaseHelper).show(primaryStage, user));
        reviewerRequest.setOnAction(a -> new displayStudentsRequestForReviewerRole(databaseHelper).show(primaryStage, user));
        
    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar();
        
        if(user.isCurrentRoleReviewer()) {
        	rightContainer.setPrefWidth(310);
        	toolbar.getItems().addAll(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton, reviewsListButton, rightContainer);
        }
        else if(user.isCurrentRoleInstructor()) {
        	rightContainer.setPrefWidth(260);
        	toolbar.getItems().addAll(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton, reviewerRequest, rightContainer);
        }
        else {
        	rightContainer.setPrefWidth(380);
        	toolbar.getItems().addAll(homeButton, forumsButton, reviewersListButton,messagesButton, searchButton, rightContainer);
        }
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	
        
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
    	// Set up listview to show list of question titles
        ObservableList<QuestionReview> questionItems = FXCollections.observableArrayList();
		ListView<QuestionReview> questionListview = new ListView<>(questionItems);
		
        ObservableList<AnswerReview> answerItems = FXCollections.observableArrayList();
		ListView<AnswerReview> answerListview = new ListView<>(answerItems);

		try {
		    databaseHelper.connectToDatabase(); // Connect to the database

		    if (databaseHelper.isDatabaseEmpty()) {
		        new FirstPage(databaseHelper).show(primaryStage);
		        return; // Exit early if database is empty
		    } else {
		        // Get reviews directly from the database and add to the observable list
		        List<QuestionReview> reviews = databaseHelper.getQuestionReviewsByAuthor(user.getUserName());
		        List<AnswerReview>  aReviews = databaseHelper.getAnswerReviewsByAuthor(user.getUserName());
		        
		        // Inject ratings
                Map<Integer, Double> avgRatings = databaseHelper.getAverageRatingsForAllReviews();
                for (QuestionReview review : reviews) {
                    double avg = avgRatings.getOrDefault(review.getId(), 0.0);
                    review.setAverageRating(avg);
                }
                
                Map<Integer, Double> answerRatings = databaseHelper.getAverageRatingsForAllAnswerReviews();
                for (AnswerReview review : aReviews) {
                    double avg = answerRatings.getOrDefault(review.getId(), 0.0);
                    review.setAverageRating(avg);
                }

                // Sort reviews by average rating (descending)
                reviews.sort((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));
                aReviews.sort((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));

		        questionItems.addAll(reviews);
		        answerItems.addAll(aReviews);
		        
		    }
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
        
        // Set custom cell factory to display questions in a readable way
		questionListview.setCellFactory(param -> new ListCell<QuestionReview>() {
            @Override
            protected void updateItem(QuestionReview review, boolean empty) {
                super.updateItem(review, empty);
                
                if (empty || review == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // set question for each review
            		Question q = new Question("", "", "", "");
            		
                	try {
                		q = databaseHelper.readQuestionById(review.getQuestionId());
					} catch (SQLException e) {
						e.printStackTrace();
					}
                	
                    Label contentLabel = new Label("Question : " + q.getTitle());
                    Label ratingLabel = new Label("Average Rating: " + String.format("%.1f", review.getAverageRating()));
                    contentLabel.setWrapText(true); // Enable text wrapping
                    
                    VBox cellContent = new VBox(10,contentLabel, ratingLabel);
                    setGraphic(cellContent);
                }
            }
        });
		
		answerListview.setCellFactory(param -> new ListCell<AnswerReview>() {
            @Override
            protected void updateItem(AnswerReview review, boolean empty) {
                super.updateItem(review, empty);
                
                if (empty || review == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // set question for each review
            		Answer a = new Answer(0, "", "");
            		
                	try {
                		a = databaseHelper.readAnswerById(review.getAnswerId());
					} catch (SQLException e) {
						e.printStackTrace();
					}
                	
                    Label contentLabel = new Label("Answer : " + a.getContent());
                    Label ratingLabel = new Label("Average Rating: " + String.format("%.1f", review.getAverageRating()));
                    contentLabel.setWrapText(true); // Enable text wrapping
                    
                    VBox cellContent = new VBox(10,contentLabel, ratingLabel);
                    setGraphic(cellContent);
                }
            }
        });
        
        // Handle button for listview upon clicking
        questionListview.setOnMouseClicked(a -> {
        	if (a.getClickCount() >= 2) {
                QuestionReview selectedItem = questionListview.getSelectionModel().getSelectedItem();
        		Question q = new Question("", "", "", "");
        		
				try {
					q = databaseHelper.readQuestionById(selectedItem.getQuestionId());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

                if(selectedItem != null) {
                	try {
                		// take person to page of question
						new IndividualReviewPage(databaseHelper).show(primaryStage, user, q, selectedItem);
                		System.out.println("clicked");
					} catch (SQLException e) {
						e.printStackTrace();
					}
                }
        	}
        });
        
        answerListview.setOnMouseClicked(a -> {
            if (a.getClickCount() >= 2) {
                AnswerReview selectedItem = answerListview.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        Answer answer = databaseHelper.readAnswerById(selectedItem.getAnswerId());
                        if (answer != null) {
                            Question q = databaseHelper.readQuestionById(answer.getQuestionId());
                            // Navigate to the individual answer review page
                            new IndividualReviewAnswerPage(databaseHelper).show(primaryStage, user,answer, selectedItem);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
	    // - - - - - - - - - - - - - - - CONTENT END  - - - - - - - - - - - - - - 

        
        

        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - - 
     // Create TabPane for different review types
        TabPane tabPane = new TabPane();
                
        // Tab for Question Reviews
        Tab questionReviewsTab = new Tab("Question Reviews");
        questionReviewsTab.setClosable(false);
                
        // Tab for Answer Reviews
        Tab answerReviewsTab = new Tab("Answer Reviews");
        answerReviewsTab.setClosable(false);
        
        questionReviewsTab.setContent(questionListview);
        answerReviewsTab.setContent(answerListview);

        // Add the tabs to the TabPane
        tabPane.getTabs().addAll(questionReviewsTab, answerReviewsTab);

        // Use tabPane in the main layout
        VBox centerContent = new VBox(10, new Label("My Reviews"), tabPane);
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
