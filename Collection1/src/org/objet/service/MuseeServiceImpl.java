package org.objet.service;

import java.util.List;

import org.objet.dao.MuseeDAO;
import org.objet.dao.MuseeDAOImpl;
import org.objet.entity.Musee;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Concrète implémentation de l'Interface service Musée du modèle DAO de 
 *         l'application 'Collection'.
 */
public class MuseeServiceImpl implements MuseeService {

	private MuseeDAO museeDAO = new MuseeDAOImpl();

	@Override
	public void addMusee(Musee musee) {
		museeDAO.addMusee(musee);
	}

	@Override
	public List<Musee> listMusee() {
		return museeDAO.listMusee();
	}

	@Override
	public void removeMusee(Integer id) {
		museeDAO.removeMusee(id);
	}

	@Override
	public void updateMusee(Musee musee) {
		museeDAO.updateMusee(musee);
	}

	@Override
	public List<String> listNomMusee() {
		return museeDAO.listNomMusee();
	}

}
