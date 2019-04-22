package org.contact.service;

import java.util.List;

import org.contact.entity.Contact;

// Model Layer: Services
/* The ContactService.java interface provides cohesive, 
 * high-level logic for related parts of the application. 
 * This layer is invoked directly by the Controller and 
 * View layers.
 */

// ContactService.java

public interface ContactService {

	public void addContact(Contact contact);

	public List<Contact> listContact();

	public void removeContact(Integer id);

	public void updateContact(Contact contact);
	
}
