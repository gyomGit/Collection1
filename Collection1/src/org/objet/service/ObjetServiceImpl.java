package org.objet.service;

import java.util.List;

import org.objet.dao.ObjetDAO;
import org.objet.dao.ObjetDAOImpl;
import org.objet.entity.Objet;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Concrète implémentation de l'Interface service objet du modèle DAO de 
 *         l'application 'Collection'.
 */
public class ObjetServiceImpl implements ObjetService {

	private ObjetDAO objetDAO = new ObjetDAOImpl();

	@Override
	public void addObjet(Objet objet) {
		objetDAO.addObjet(objet);
	}

	@Override
	public List<Objet> listObjet() {
		return objetDAO.listObjet();
	}

	@Override
	public void removeObjet(Integer id) {
		objetDAO.removeObjet(id);
	}

	@Override
	public void updateObjet(Objet objet) {
		objetDAO.updateObjet(objet);
	}

	@Override
	public List<String> listIdentification() {
		return objetDAO.listIdentification();
	}

	@Override
	public List<String> listInventaire() {
		return objetDAO.listInventaire();
	}

}
