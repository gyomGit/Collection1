package application;

import java.util.List;

import org.objet.entity.Musee;
import org.objet.service.MuseeService;
import org.objet.service.MuseeServiceImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *          Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *          que j'appelle 'Collection' servant à gérer les objets dans les
 *          Musées.
 * 
 *          Classe Contrôleur Musée de l'application 'Collection'
 */
public class MuseeController {
	
	private MuseeService museeService = new MuseeServiceImpl();
	private ObservableList<Musee> museeList = FXCollections.observableArrayList();

	/**
	 * Méthode servant à passer une requète de type Insert à la couche service du
	 * modèle DAO.
	 * 
	 * @param musee à ajouter dans la base de données
	 */
	public void addMusee(Musee musee) {
		museeService.addMusee(musee);
	}

	/**
	 * Méthode servant à passer une requète de type Select à la couche service du
	 * modèle DAO.
	 * 
	 * @return la Liste des Musées
	 */
	public ObservableList<Musee> getMuseeList() {
		if (!museeList.isEmpty())
			museeList.clear();
		museeList = FXCollections.observableList((List<Musee>) museeService.listMusee());
		return museeList;
	}

	/**
	 * Méthode servant à passer une requète de type Delete à la couche service du
	 * modèle DAO.
	 * 
	 * @param id identifiant du Musee museeId
	 */
	public void removeMusee(Integer id) {
		museeService.removeMusee(id);
	}
	
	/**
	 * Méthode servant à passer une requète de type Update à la couche service du
	 * modèle DAO.
	 * 
	 * @param musee à mettre à jour
	 */
	public void updateMusee(Musee musee) {
		museeService.updateMusee(musee);
	}
}
