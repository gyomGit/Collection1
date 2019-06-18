package org.objet.dao;

import java.util.List;

import org.objet.entity.Musee;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Interface de la couche persistance Musee du modèle DAO de l'application 
 *         'Collection'
 */
public interface MuseeDAO {

	/**
	 * Méthode servant à passer une requète de type Insert à la couche precistance
	 * du modèle DAO.
	 * 
	 * @param musee à ajouter dans la base de données
	 */
	public void addMusee(Musee musee);

	/**
	 * Méthode servant à passer une requète de type Select à la couche persistance
	 * du modèle DAO.
	 * 
	 * @return la liste des Musées
	 */
	public List<Musee> listMusee();

	/**
	 * Méthode servant à passer une requète de type Delete à la couche persitance du
	 * modèle DAO.
	 * 
	 * @param id l'identifiant du Musee: museeId
	 */
	public void removeMusee(Integer id);

	/**
	 * Méthode servant à passer une requète de type Update à la couche persitance du
	 * modèle DAO.
	 * 
	 * @param musee à mettre à jour
	 */
	public void updateMusee(Musee musee);

	/**
	 * Méthode servant à passer une requète de type Select à la couche persistance
	 * du modèle DAO.
	 * 
	 * @return la Liste des des noms de Musées
	 */
	public List<String> listNomMusee();

}
