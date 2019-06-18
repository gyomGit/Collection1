package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Classe de démarrage de l'application 'Collection'
 */
public class Main extends Application {

	/**
	 * Méthode principale de démarrage
	 * 
	 * @param args
	 * @throws Exception
	 */
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
