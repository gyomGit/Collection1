package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Classe Login de l'application 'Collection'
 *         en relation avec les données graphiques du fichier Login.fxml
 */
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

	/**
	 * Méthode qui permet de comparer les données user name et password entrées par
	 * l'utilsateur. Si elles sont correctes: charge l'IHM de l'application qui
	 * démarre.
	 * 
	 * @throws IOException
	 */
	public void Login() throws IOException {
		if (userNameField.getText().equals("lambda") && passWordField.getText().equals("marty")) {
			statusLabel.setText("Login Success");

			Stage primaryStage = new Stage();
			primaryStage.setTitle("Collection");

			Parent root = FXMLLoader.load(getClass().getResource("/application/Main2.fxml"));

			Scene scene = new Scene(root, 900, 700);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.show();
			closeLogin();

		} else {
			statusLabel.setText("Login Failed");
		}
	}

	/**
	 * Méthode qui ferme la fenêtre de Login si l'IHM est chargée et que
	 * l'application démarre.
	 */
	public void closeLogin() {
		loginStage = (Stage) loginButton.getScene().getWindow();
		loginStage.close();
	}
}
