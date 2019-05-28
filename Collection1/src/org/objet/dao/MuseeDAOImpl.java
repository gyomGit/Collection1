package org.objet.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.objet.entity.HibernateUtil;
import org.objet.entity.Musee;

/* A concrete implementation of this interface is 
 * provided by the ContactDAOImpl.java class. This 
 * approach isolates domain-specific objects and data 
 * types, abstracting the application needs and how they 
 * are satisfied.
 */

// ContactDAOImpl.java

public class MuseeDAOImpl implements MuseeDAO{
	
     @Override
     public void addMusee(Musee musee) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          s.save(musee);
          s.getTransaction().commit();
          s.close();
     }

     @SuppressWarnings("unchecked")
	@Override
     public List<Musee> listMusee() {
          List<Musee> list = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          list = s.createQuery("from Musee").list();
          s.getTransaction().commit();
          s.close();
          return list;
     }

     @Override
     public void removeMusee(Integer id) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          Musee m = (Musee)s.load(Musee.class , id);
          s.delete(m);
          s.getTransaction().commit();
          s.close();
     }

     @Override
     public void updateMusee(Musee musee) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          s.update(musee);
          s.getTransaction().commit();
          s.close();
     }

     @SuppressWarnings("unchecked")
	@Override
     public List<String> listNomMusee() {
          List<String> listI = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          listI = s.createQuery("select nomMusee from Musee").list();
          s.getTransaction().commit();
          s.close();
          return listI;
     }
     
}
