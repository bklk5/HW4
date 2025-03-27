package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Node;


public class ConversationPage {
	private final DatabaseHelper databaseHelper;

    public ConversationPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user, User otherUser) {  
    	// - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	// Set up buttons for top nav bar 
    	Button homeButton = new Button("Home");
    	Button forumsButton = new Button("Forums");
    	Button messagesButton = new Button("Messages");
    	
    	homeButton.setOnAction(a -> new HomePage(databaseHelper).show(primaryStage, user));
    	forumsButton.setOnAction(a -> new Forums(databaseHelper).show(primaryStage, user));
    	messagesButton.setOnAction(a -> new MessagesPage(databaseHelper).show(primaryStage,user));
    	
    	// Create the Top Navigation Bar
        ToolBar toolbar = new ToolBar(homeButton, forumsButton, messagesButton);
        // - - - - - - - - - - - - - - - NAV BAR - - - - - - - - - - - - - - 
    	
        
        
        // - - - - - - - - - - - - - - - CONTENT - - - - - - - - - - - - - - 
        Label header = new Label("Messaging : " + otherUser.getUserName());
	    header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	    
	    TextField messageBox = new TextField();
	    messageBox.setPromptText("Type message here");
	    messageBox.setPrefHeight(50);
	    messageBox.setPrefWidth(600);
	    
	    Button sendButton = new Button("Send >");
	    
	    sendButton.setOnAction(a -> {
	    	String content = messageBox.getText();
	    	Message message = new Message(user.getUserName(), otherUser.getUserName(), content);
	    	databaseHelper.sendMessage(message);
	    	new ConversationPage(databaseHelper).show(primaryStage, user, otherUser);
	    });

    	// Set up listview to show list of question titles
    	ObservableList<Message> items = FXCollections.observableArrayList();
    	ListView<Message> listView = new ListView<>(items);
    	
    	List<Message> mList = new ArrayList<>();

        
    	// connect to database and get conversation between users
        try {
            databaseHelper.connectToDatabase(); 

            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
                return; // Exit early if database is empty
            } else {
            	
            	// Add question titles to listview 
            	mList = databaseHelper.getConversation(user.getUserName(), otherUser.getUserName());
                items.addAll(mList); 

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        // Set custom cell factory to display questions in a readable way
        listView.setCellFactory(param -> new javafx.scene.control.ListCell<Message>() {
            @Override
            protected void updateItem(Message m, boolean empty) {
                super.updateItem(m, empty);
                if (empty || m == null) {
                    setText(null);
                } else {
                    setText(m.getSender() + " : " + m.getContent());
                    setStyle("-fx-padding:10px 20px;");
                }
            }
        });
        
        // Handle button for listview upon clicking
        listView.setOnMouseClicked(a -> {
        	if (a.getClickCount() >= 2) {
                Message selectedItem = listView.getSelectionModel().getSelectedItem();
                
                if(selectedItem != null) {
                	// take person to page of question
					System.out.println(selectedItem.getContent());
                }
        	}
        });
        
        listView.scrollTo(items.size() - 1);
	    // - - - - - - - - - - - - - - - CONTENT END  - - - - - - - - - - - - - - 

        
        

        // - - - - - - - - - - - - - - - GENERAL LAYOUT FOR PAGES - - - - - - - - - - - - - - 
        HBox sendMessageContainer = new HBox(50, messageBox, sendButton);
        
        VBox centerContent = new VBox(10, header, listView, sendMessageContainer);
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
