package org.contact.service;

import java.util.List;

import org.contact.entity.Musee;

// Model Layer: Services
/* The ContactService.java interface provides cohesive, 
 * high-level logic for related parts of the application. 
 * This layer is invoked directly by the Controller and 
 * View layers.
 */

// ContactService.java

public interface MuseeService {

	public void addMusee(Musee musee);

	public List<Musee> listMusee();

	public void removeMusee(Integer id);

	public void updateMusee(Musee musee);
	
	public List<String> listNomMusee();
	
}
