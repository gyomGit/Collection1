package org.contact.dao;

import java.util.List;
import org.contact.entity.Contact;

/* ContactDAO.java is an abstract interface of our persistence
 * mechanism. It supports operation without exposing any details 
 * of the database. This interface provides a mapping from 
 * application calls to the persistence layer.
 */

// ContactDAO.java

public interface ContactDAO {
	
	public void addContact(Contact contact);

	public List<Contact> listContact();

	public void removeContact(Integer id);

	public void updateContact(Contact contact);
}
