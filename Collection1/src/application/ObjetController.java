package application;

import java.util.List;

import org.objet.entity.Objet;
import org.objet.service.ObjetService;
import org.objet.service.ObjetServiceImpl;

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
 *          Classe Contrôleur Objet de l'application 'Collection'
 */
public class ObjetController {
	private ObjetService objetService = new ObjetServiceImpl();
	private ObservableList<Objet> objetList = FXCollections.observableArrayList();

	/**
	 * Méthode servant à passer une requète de type Insert à la couche service du
	 * modèle DAO.
	 * 
	 * @param objet à ajouter dans la base de données
	 */
	public void addObjet(Objet objet) {
		objetService.addObjet(objet);
	}

	/**
	 * Méthode servant à passer une requète de type Select à la couche service du
	 * modèle DAO.
	 * 
	 * @return la Liste des Objets
	 */

	public ObservableList<Objet> getObjetList() {
		if (!objetList.isEmpty())
			objetList.clear();
		objetList = FXCollections.observableList((List<Objet>) objetService.listObjet());
		return objetList;
	}

	/**
	 * Méthode servant à passer une requète de type Delete à la couche service du
	 * modèle DAO.
	 * 
	 * @param id l'identifiant de l'objet: objetId
	 */
	public void removeObjet(Integer id) {
		objetService.removeObjet(id);
	}

	/**
	 * Méthode servant à passer une requète de type Update à la couche service du
	 * modèle DAO.
	 * 
	 * @param objet à mettre à jour
	 */
	public void updateObjet(Objet objet) {
		objetService.updateObjet(objet);
	}
}
