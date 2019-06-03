package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	
    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passWordField;

    @FXML
    private Label statusLabel;
    
    @FXML
    private Button loginButton;
    
    Stage loginStage = null;
    
    
	public void Login(ActionEvent event) throws IOException {
		if (userNameField.getText().equals("lambda") && passWordField.getText().equals("marty")) {
			statusLabel.setText("Login Success");
			
			Stage primaryStage = new Stage();
			primaryStage.setTitle("Collection");
			
			Parent root = FXMLLoader.load(getClass().getResource("/application/Main2.fxml"));

			Scene scene = new Scene(root, 1024, 768);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.show();
			closeLogin();
			
			
		}else {
			statusLabel.setText("Login Failed");
		}
	}
	
    public void closeLogin() {
    	loginStage = (Stage) loginButton.getScene().getWindow();
    	loginStage.close();
    }

}
