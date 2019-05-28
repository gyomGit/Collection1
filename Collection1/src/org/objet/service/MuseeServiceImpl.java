package org.objet.service;

import java.util.List;

import org.objet.dao.MuseeDAO;
import org.objet.dao.MuseeDAOImpl;
import org.objet.entity.Musee;


/* ObjetServiceImpl.java is the concrete implementation 
 * of the ObjetService.java interface. It provides a 
 * public interface of underlying model objects.
 */

// ObjetServiceImpl.java

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
