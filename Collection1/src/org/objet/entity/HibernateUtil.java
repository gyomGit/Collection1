package org.objet.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

// The class HibernateUtil.java contains all the configuration requirements
// for our project to interact with the database. The Hibernate configuration
// can also be established through XML, but in this case I used annotation-based configuration.

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *          Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *          que j'appelle 'Collection' servant à gérer les objets dans les
 *          Musées.
 * 
 *          Classe de configurations requises pour une interaction entre la base
 *          de données et l'application 'Collection'
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static final ServiceRegistry serviceRegistry;

	static {
		try {
			Configuration config = getConfiguration();
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			config.setSessionFactoryObserver(new SessionFactoryObserver() {

				private static final long serialVersionUID = 1L;

				@Override
				public void sessionFactoryCreated(SessionFactory factory) {

				}

				@Override
				public void sessionFactoryClosed(SessionFactory factory) {
					StandardServiceRegistryBuilder.destroy(serviceRegistry);
				}
			});
			sessionFactory = config.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session openSession() {

		return sessionFactory.openSession();
	}

	private static Configuration getConfiguration() {
		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(Objet.class);
		cfg.addAnnotatedClass(Musee.class);
		cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://127.0.0.1:5432/comptesdb");
		cfg.setProperty("hibernate.connection.username", "lambda");
		cfg.setProperty("hibernate.connection.password", "marty");
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		cfg.setProperty("hibernate.current_session_context_class", "thread");
		return cfg;
	}
}
