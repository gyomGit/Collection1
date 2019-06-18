package test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.objet.entity.Musee;
import org.objet.entity.Objet;

/**
 * @author Nom: Balourdet, Prenom: Guillaume
 * @version 0.1
 * 
 *          Projet du 22 juin 2019 CNAM Implementation d'une application en Java
 *          que j'appelle 'Collection' servant à gérer les objets dans les
 *          Musées.
 * 
 *          Classe de configurations requises pour une interaction entre la
 *          in_memory database appelée HSQLDB et l'application 'Collection' afin
 *          de pouvoir réaliser des tests avec Junit 5 sans inter agir avec la
 *          réelle base de données.
 */
public class HibernateUtilTest {

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
		cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
		cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:objet");
		cfg.setProperty("hibernate.connection.username", "none");
		cfg.setProperty("hibernate.connection.password", "none");
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		cfg.setProperty("hibernate.current_session_context_class", "thread");
		return cfg;
	}
}
