package org.objet.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.objet.entity.HibernateUtil;
import org.objet.entity.Objet;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *         Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *         que j'appelle 'Collection' servant à gérer les objets dans les Musées.
 * 
 *         Concrète implémentation de l'Interface presistance objet du modèle DAO de 
 *         l'application 'Collection'.
 */
public class ObjetDAOImpl implements ObjetDAO{
	
     @Override
     public void addObjet(Objet objet) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          s.save(objet);
          s.getTransaction().commit();
          s.close();
     }

     @SuppressWarnings("unchecked")
	@Override
     public List<Objet> listObjet() {
          List<Objet> list = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          list = s.createQuery("from Objet").list();
          s.getTransaction().commit();
          s.close();
          return list;
     }

     @Override
     public void removeObjet(Integer id) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          Objet c = (Objet)s.load(Objet.class , id);
          s.delete(c);
          s.getTransaction().commit();
          s.close();
     }

     @Override
     public void updateObjet(Objet objet) {
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          s.update(objet);
          s.getTransaction().commit();
          s.close();
     }

     @SuppressWarnings("unchecked")
	@Override
     public List<String> listIdentification() {
          List<String> listI = new ArrayList<>();
          Session s = HibernateUtil.openSession();
          s.beginTransaction();
          listI = s.createQuery("select identification from Objet").list();
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
          listInv = s.createQuery("select inventaire from Objet").list();
          s.getTransaction().commit();
          s.close();
          return listInv;
     }
}
