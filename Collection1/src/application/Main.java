package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//View Layer of MVC
/* Our front-end object is implemented in JavaFX. Main.java acts as a View layer for our 
* application. The View layer translates data for visual rendering in response to the client. 
* The data is supplied primarily by the Controller, ObjetController.java in this case.
*/

public class Main extends Application {

	public static void main(String[] args) throws Exception {
		Application.launch(args);

	}

	@Override
	public void start(Stage loginStage) throws Exception {
		loginStage.setTitle("Collection");
		Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));

		Scene scene = new Scene(root, 300, 300);


		loginStage.setScene(scene);
		loginStage.show();
	}

}
