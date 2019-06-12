package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.objet.entity.Musee;
import org.objet.entity.Objet;

class TestAddObjetInMemoryDB {

	static Session sessionTest = HibernateUtilTest.openSession();

	@BeforeAll
	public static final void initHandleAddMuseesForTest() {

//		Session sessionTest = HibernateUtilTest.openSession();

		sessionTest.beginTransaction();

		// Add Musees test
		Musee MuseeTest = new Musee();
		MuseeTest.setNomMusee("Musée de SalagonTEST");
		MuseeTest.setEmailMusee("musee.salagontest@le04.test");
		MuseeTest.setTelMusee("TEST 75 70 50");
		MuseeTest.setAdressMusee("Le Prieuré 04300 TEST");
		MuseeTest.setPrefixeMusee("TEST_SLG");

		// Add Musees 1 test
		Musee MuseeTest1 = new Musee();
		MuseeTest1.setNomMusee("Musée de ForcalquierTEST");
		MuseeTest1.setEmailMusee("musee.forcalqiertest@le04.test");
		MuseeTest1.setTelMusee("TEST 75 00 13");
		MuseeTest1.setAdressMusee("5 Place du Bourguet 04300 TEST");
		MuseeTest1.setPrefixeMusee("TEST_FOR");

		sessionTest.save(MuseeTest);
		sessionTest.save(MuseeTest1);

		// Add objet test
		Objet objetTest = new Objet();
		byte[] imageTest = new byte[2000];
		objetTest.setObjetId(1);
		objetTest.setIdentification("marteau TEST");
		objetTest.setPrefixeMusee("TEST_SLG");
		objetTest.setMusee(MuseeTest);
		objetTest.setSelected(false);
		objetTest.setInventaire("TEST_2002.9.234");
		objetTest.setLocalisation("TEST_B53");
		objetTest.setImageName("marteau_TEST.jpg");
		objetTest.setImage(imageTest);

		// Add objet 1 test
		Objet objetTest1 = new Objet();
		byte[] imageTest1 = new byte[2000];
		objetTest1.setObjetId(1);
		objetTest1.setIdentification("charrue TEST");
		objetTest1.setPrefixeMusee("TEST_SLG");
		objetTest1.setMusee(MuseeTest1);
		objetTest1.setSelected(false);
		objetTest1.setInventaire("TEST_2002.9.3");
		objetTest1.setLocalisation("TEST_B49");
		objetTest1.setImageName("charrue_TEST.jpg");
		objetTest1.setImage(imageTest1);

		sessionTest.save(objetTest);
		sessionTest.save(objetTest1);

		sessionTest.getTransaction().commit();

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Le Musée à bien été ajouté dans la base de données")
	public void testHandleAddMusee() {
		List<Musee> TestMuseelist = new ArrayList<>();
		TestMuseelist = sessionTest.createQuery("from Musee").list();
		assertEquals(2, TestMuseelist.size());

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Les 2 objets on bien été ajoutés dans la base de données")
	public void testHandleAddObjet() {
		List<Objet> TestObjetList = new ArrayList<>();
		TestObjetList = sessionTest.createQuery("from Objet").list();
		assertEquals(2, TestObjetList.size());

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Le nom du premier objet correspond bien à celui dans la base")
	public void testObjetName() {
		List<Objet> TestObjetList = new ArrayList<>();
		TestObjetList = sessionTest.createQuery("from Objet").list();
		assertEquals("marteau TEST", TestObjetList.get(0).getIdentification());

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Le nom du premier Musée correspond bien à celui dans la base")
	public void testMuseeName() {
		List<Musee> TestMuseelist = new ArrayList<>();
		TestMuseelist = sessionTest.createQuery("from Musee").list();
		assertEquals("Musée de SalagonTEST", TestMuseelist.get(0).getNomMusee());

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Le nom du Musée correspond bien à celui du premier objet dans la base")
	public void testFirstObjetWithMusee() {
		List<Objet> TestObjetList = new ArrayList<>();
		TestObjetList = sessionTest.createQuery("from Objet").list();
		assertEquals("Musée de SalagonTEST", TestObjetList.get(0).getMusee().getNomMusee());
	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Le nom du Musée correspond bien à celui du deuxième objet dans la base")
	public void testSecondObjetWithMusee() {
		List<Objet> TestObjetList = new ArrayList<>();
		TestObjetList = sessionTest.createQuery("from Objet").list();
		assertEquals("Musée de ForcalquierTEST", TestObjetList.get(1).getMusee().getNomMusee());
	}

	@AfterAll
	public static void endSessionTesting() {

		sessionTest.close();
	}

}
