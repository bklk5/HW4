package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

public class SearchQuestions {
    private final DatabaseHelper databaseHelper;
    private ListView<Question> questionsListView = new ListView<>(); 
    private TextField searchField = new TextField();

    public SearchQuestions(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
        BorderPane layout = new BorderPane(); 

    	// - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	// Set up buttons for top nav bar 
    	Button homeButton = new Button("Home");
    	Button forumsButton = new Button("Forums");
    	Button navSearch = new Button("Search");
    	Button reviewersListButton = new Button("Trusted Reviewers List");
    	Button messagesButton = new Button("Messages");
    	Button logoutButton = new Button("Logout");
    	
    	// container to right align logout button
    	HBox rightContainer = new HBox(logoutButton);
    	rightContainer.setPrefWidth(380);
    	rightContainer.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
    	
    	homeButton.setOnAction(a -> new HomePage(databaseHelper).show(primaryStage, user));
    	forumsButton.setOnAction(a -> new Forums(databaseHelper).show(primaryStage, user));
    	navSearch.setOnAction(e -> new SearchQuestions(databaseHelper).show(primaryStage, user));
    	// set on action with reviewersListButton
    	messagesButton.setOnAction(a -> new MessagesPage(databaseHelper).show(primaryStage,user));
        logoutButton.setOnAction(a -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));

    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar(homeButton, forumsButton, reviewersListButton,messagesButton, navSearch,rightContainer);
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 

       //SEARCH BAR
        searchField.setPromptText("Enter keywords to search...");
        Button searchButton = new Button("Search");
        HBox searchBar = new HBox(10, searchField, searchButton);
        searchBar.setStyle("-fx-padding: 10px;");

        //SETTING UP THE QUESTIONS IN LIST VIEW
        VBox questionBox = new VBox(10, new Label("Search Results:"), questionsListView);
        questionBox.setStyle("-fx-padding: 10px;");

        
        questionsListView.setCellFactory(lv -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question question, boolean empty) {
                super.updateItem(question, empty);
                if (empty || question == null) {
                    setText(null);
                } else {
                    setText(question.getTitle() + " - " + question.getContent());
                }
            }
        });
 

        // WHEN YOU DOUBLE CLICK, IT SHOULD OPEN THE QUESTION
        questionsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                Question selectedItem = questionsListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        new IndividualQuestionPage(databaseHelper).show(primaryStage, user, selectedItem);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        homeButton.setOnAction(e -> new HomePage(databaseHelper).show(primaryStage, user));
        forumsButton.setOnAction(e -> new Forums(databaseHelper).show(primaryStage, user));
        searchButton.setOnAction(e -> performSearch());
        
        layout.setTop(toolbar);
        layout.setCenter(questionBox);
        layout.setBottom(searchBar);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Search Questions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    	//SEARCH FUNCTION 
    private void performSearch() {
    	
        String searchText = searchField.getText().trim().toLowerCase();
        
        questionsListView.getItems().clear();
        try {
            List<Question> questions = databaseHelper.getQuestions(); //FETCHES AND LISTS ALL THE QUESTIONS
            for (Question question : questions) {
                if (question.getTitle().toLowerCase().contains(searchText) || question.getContent().toLowerCase().contains(searchText)) {
                    questionsListView.getItems().add(question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            questionsListView.getItems().add(new Question("Error loading questions", "", searchText, searchText)); //ERROR SYSTEM
        }
        
        
        
    }
}