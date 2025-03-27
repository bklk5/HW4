package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class NewPassword extends Application {
	public final static double WINDOW_WIDTH = 500;
	public final static double WINDOW_HEIGHT = 430;
		
	public NewPasswordInterface theGUI;
		
		@Override
		public void start (Stage theStage) throws Exception {
			Pane theRoot = new Pane();							// Create a pane within the window
			theGUI = new NewPasswordInterface (theRoot);		// Create the Graphical User Interface
			Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT);	// Create the scene
			theStage.setScene(theScene);						// Set the scene on the stage
			theStage.show();									// Show the stage to the user
			
			// When the stage is shown to the user, the pane within the window is visible.  This means
			// that the labels, fields, and buttons of the Graphical User Interface (GUI) are visible 
			// and it is now possible for the user to select input fields and enter values into them, 
			// click on buttons, and read the labels, the results, and the error messages.
	}
		
		public static void main(String[] args) {
			launch(args);
		}
}