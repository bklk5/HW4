package application;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.*;



  // To list user info
   public class AdminUserListPage{
   private final DatabaseHelper databaseHelper;

   
   	
   	public AdminUserListPage(DatabaseHelper databaseHelper) {
   		this.databaseHelper = databaseHelper;
   		///this.table = new TableView<> ();
   	}
   	
   // label to display "User List"
      public void show(Stage primaryStage ) {
	  VBox layout = new VBox();
	  layout.setPadding(new Insets(10,10,10,10));
	 
	  Label userListLabel = new Label("User List");
	  userListLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	 
	  Label listOfUsers = new Label("");
	  
	  Button backButton = new Button("<--");
	  
	  ScrollPane scrolListOfUsers = new ScrollPane();
	  scrolListOfUsers.setMinHeight(200);
	  scrolListOfUsers.setMaxHeight(200);
		 
	 
	  listOfUsers.setText(databaseHelper.retrieveUsersInfo());
	
	  scrolListOfUsers.setContent(listOfUsers);
	   // Go back to the main page
	  backButton.setOnAction(a ->{
    	new AdminHomePage(databaseHelper).show(primaryStage);
    	
	  });
	  
	  layout.getChildren().addAll(userListLabel, scrolListOfUsers,backButton);
	  
	  //To set the scene and show the table
	  Scene scene = new Scene(layout,600,400);
	  primaryStage.setScene(scene);
	  primaryStage.show();
      }
      
      

	   
    }
  




