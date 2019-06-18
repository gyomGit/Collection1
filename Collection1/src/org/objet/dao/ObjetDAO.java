package org.objet.dao;

import java.util.List;

import org.objet.entity.Objet;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Interface de la couche persistance objet du modèle DAO de l'application 
 *         'Collection'
 */
public interface ObjetDAO {
	
	/**
	 * Méthode servant à passer une requète de type Insert à la couche precistance
	 * du modèle DAO.
	 * 
	 * @param objet à ajouter dans la base de données
	 */
	public void addObjet(Objet objet);
	
	/**
	 * Méthode servant à passer une requète de type Select à la couche persistance
	 * du modèle DAO.
	 * 
	 * @return la liste des objets
	 */
	public List<Objet> listObjet();

	/**
	 * Méthode servant à passer une requète de type Delete à la couche persitance du
	 * modèle DAO.
	 * 
	 * @param id l'identifiant de l'objet: objetId
	 */
	public void removeObjet(Integer id);
	
	/**
	 * Méthode servant à passer une requète de type Update à la couche persitance du
	 * modèle DAO.
	 * 
	 * @param objet à mettre à jour
	 */
	public void updateObjet(Objet objet);

	/**
	 * Méthode servant à passer une requète de type Select à la couche persistance
	 * du modèle DAO.
	 * 
	 * @return la Liste des identifications des objets
	 */
	public List<String> listIdentification();

	/**
	 * Méthode servant à passer une requète de type Select à la couche persistance
	 * du modèle DAO.
	 * 
	 * @return la Liste des numéros d'inventaire des objets
	 */
	public List<String> listInventaire();
	
}
