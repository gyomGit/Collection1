package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//View Layer of MVC
/* Our front-end object is implemented in JavaFX. Main.java acts as a View layer for our 
* application. The View layer translates data for visual rendering in response to the client. 
* The data is supplied primarily by the Controller, ContactController.java in this case.
*/

public class Main extends Application {

	public static void main(String[] args) throws Exception {
		Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Collection");
		Parent root = FXMLLoader.load(getClass().getResource("/application/Main2.fxml"));

		Scene scene = new Scene(root, 1200, 700);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
