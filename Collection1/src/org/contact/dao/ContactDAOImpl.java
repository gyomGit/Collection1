package org.contact.dao;

import java.util.ArrayList;
import java.util.List;
import org.contact.entity.Contact;
import org.contact.entity.HibernateUtil;
import org.hibernate.Session;

/* A concrete implementation of this interface is 
 * provided by the ContactDAOImpl.java class. This 
 * approach isolates domain-specific objects and data 
 * types, abstracting the application needs and how they 
 * are satisfied.
 */

// ContactDAOImpl.java

public class ContactDAOImpl implements ContactDAO{
	
     @Override
     public void addContact(Contact contact) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          s.save(contact);
          s.getTransaction().commit();
          s.close();
     }

     @SuppressWarnings("unchecked")
	@Override
     public List<Contact> listContact() {
          List<Contact> list = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          list = s.createQuery("from Contact").list();
          s.getTransaction().commit();
          s.close();
          return list;
     }

     @Override
     public void removeContact(Integer id) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          Contact c = (Contact)s.load(Contact.class , id);
          s.delete(c);
          s.getTransaction().commit();
          s.close();
     }

     @Override
     public void updateContact(Contact contact) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          s.update(contact);
          s.getTransaction().commit();
          s.close();
     }

     @SuppressWarnings("unchecked")
	@Override
     public List<String> listIdentification() {
          List<String> listI = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          listI = s.createQuery("select identification from Contact").list();
          s.getTransaction().commit();
          s.close();
          return listI;
     }
     
     @SuppressWarnings("unchecked")
	@Override
     public List<String> listInventaire() {
          List<String> listInv = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          listInv = s.createQuery("select inventaire from Contact").list();
          s.getTransaction().commit();
          s.close();
          return listInv;
     }
}
