package org.objet.dao;

import java.util.List;

import org.objet.entity.Musee;

/* ObjetDAO.java is an abstract interface of our persistence
 * mechanism. It supports operation without exposing any details 
 * of the database. This interface provides a mapping from 
 * application calls to the persistence layer.
 */

// ObjetDAO.java

public interface MuseeDAO {
	
	public void addMusee(Musee musee);

	public List<Musee> listMusee();

	public void removeMusee(Integer id);

	public void updateMusee(Musee musee);

	public List<String> listNomMusee();
	
}
