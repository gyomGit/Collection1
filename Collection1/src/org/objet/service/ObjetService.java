package org.objet.service;

import java.util.List;

import org.objet.entity.Objet;

// Model Layer: Services
/* The ObjetService.java interface provides cohesive, 
 * high-level logic for related parts of the application. 
 * This layer is invoked directly by the Controller and 
 * View layers.
 */

// ObjetService.java

public interface ObjetService {

	public void addObjet(Objet objet);

	public List<Objet> listObjet();

	public void removeObjet(Integer id);

	public void updateObjet(Objet objet);
	
	public List<String> listIdentification();
	
	public List<String> listInventaire();
	
}
