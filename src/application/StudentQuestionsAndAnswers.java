package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentQuestionsAndAnswers {
	private final DatabaseHelper databaseHelper;

    public StudentQuestionsAndAnswers (DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user, User studentInReview) {  
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
    	// Set up listview to show list of question titles
    	ObservableList<Object> items = FXCollections.observableArrayList();
    	ListView<Object> listView = new ListView<>(items);
    	

        
        try {
            databaseHelper.connectToDatabase(); // Connect to the database

            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
                return; // Exit early if database is empty
            } else {
            	
                items.addAll(databaseHelper.getQuestionByAuthor(studentInReview.getUserName())); 
                items.addAll(databaseHelper.readAnswersByAuthor(studentInReview.getUserName())); 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        // Set custom cell factory to display questions in a readable way
        listView.setCellFactory(param -> new javafx.scene.control.ListCell<Object>() {
            @Override
            protected void updateItem(Object i, boolean empty) {
                super.updateItem(i, empty);
                if (empty || i == null) {
                    setText(null);
                } 
                else if(i instanceof Question) {
                	Question temp  = (Question) i;
                	setText("Question title: " + temp.getTitle());
                }
       
                else {
                	Answer temp  = (Answer) i;
                    setText("Answer: " + temp.getContent());
                }
            }
        });

       
        
        // Handle button for listview upon clicking
        listView.setOnMouseClicked(a -> {
        	if (a.getClickCount() >= 2) {
                Object selectedItem = listView.getSelectionModel().getSelectedItem();
             // take instructor to the specific answer, question page made by the reviewed student
                if(selectedItem != null) {
                	
                	//check if the object is of type question or answer
                	if(selectedItem instanceof Question) {
                		Question temp = (Question) selectedItem;
                		try {
							new IndividualQuestionPage(databaseHelper).show(primaryStage, user, temp);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                	}
                	//If not a question, must be an answer
                	else {
                		Answer temp = (Answer) selectedItem;
                		try {
                			Question q = databaseHelper.readQuestionById(temp.getQuestionId());
							new IndividualQuestionPage(databaseHelper).show(primaryStage, user, q);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                	}
                }
        	}
        });
	    // - - - - - - - - - - - - - - - CONTENT END  - - - - - - - - - - - - - - 

        
        

        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - - 
        VBox centerContent = new VBox(10,listView);
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
