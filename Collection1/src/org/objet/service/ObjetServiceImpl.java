package org.objet.service;

import java.util.List;

import org.objet.dao.ObjetDAO;
import org.objet.dao.ObjetDAOImpl;
import org.objet.entity.Objet;

/* ObjetServiceImpl.java is the concrete implementation 
 * of the ObjetService.java interface. It provides a 
 * public interface of underlying model objects.
 */

// ObjetServiceImpl.java

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
