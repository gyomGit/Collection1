package org.contact.service;

import java.util.List;


import org.contact.dao.MuseeDAO;
import org.contact.dao.MuseeDAOImpl;
import org.contact.entity.Musee;


/* ContactServiceImpl.java is the concrete implementation 
 * of the ContactService.java interface. It provides a 
 * public interface of underlying model objects.
 */

// ContactServiceImpl.java

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
