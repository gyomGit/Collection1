package org.objet.service;

import java.util.List;

import org.objet.entity.Musee;

// Model Layer: Services
/* The ObjetService.java interface provides cohesive, 
 * high-level logic for related parts of the application. 
 * This layer is invoked directly by the Controller and 
 * View layers.
 */

// ObjetService.java

public interface MuseeService {

	public void addMusee(Musee musee);

	public List<Musee> listMusee();

	public void removeMusee(Integer id);

	public void updateMusee(Musee musee);
	
	public List<String> listNomMusee();
	
}
