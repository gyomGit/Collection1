package org.objet.dao;

import java.util.List;

import org.objet.entity.Objet;

/* ContactDAO.java is an abstract interface of our persistence
 * mechanism. It supports operation without exposing any details 
 * of the database. This interface provides a mapping from 
 * application calls to the persistence layer.
 */

// ContactDAO.java

public interface ObjetDAO {
	
	public void addObjet(Objet objet);

	public List<Objet> listObjet();

	public void removeObjet(Integer id);

	public void updateObjet(Objet objet);

	public List<String> listIdentification();

	public List<String> listInventaire();
	
}
