package org.objet.service;

import java.util.List;

import org.objet.entity.Objet;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Interface de la couche service objet du modèle DAO de l'application 
 *         'Collection'
 */
public interface ObjetService {

	/**
	 * Méthode servant à passer une requète de type Insert à la couche DAO du modèle
	 * DAO.
	 * 
	 * @param objet à ajouter dans la base de données
	 */
	public void addObjet(Objet objet);

	/**
	 * Méthode servant à passer une requète de type Select à la couche DAO du modèle
	 * DAO.
	 * 
	 * @return la liste des Objets
	 */
	public List<Objet> listObjet();

	/**
	 * Méthode servant à passer une requète de type Delete à la couche DAO du modèle
	 * DAO.
	 * 
	 * @param id l'identifiant de l'objet: objetId
	 */
	public void removeObjet(Integer id);

	/**
	 * Méthode servant à passer une requète de type Update à la couche DAO du modèle
	 * DAO.
	 * 
	 * @param objet à mettre à jour
	 */
	public void updateObjet(Objet objet);

	/**
	 * Méthode servant à passer une requète de type Select à la couche DAO du
	 * modèle DAO.
	 * 
	 * @return la liste des identifications des objets
	 */
	public List<String> listIdentification();
	
	/**
	 * Méthode servant à passer une requète de type Select à la couche DAO du
	 * modèle DAO.
	 * 
	 * @return la liste des numéros d'inventaire des objets
	 */
	public List<String> listInventaire();

}
