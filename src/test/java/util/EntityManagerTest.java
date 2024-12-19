package util;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import jakarta.persistence.EntityManager;

@SuppressWarnings("all")
public class EntityManagerTest extends EntityManagerFactoryTest {
	
	protected static EntityManager em;
	
	protected static Session session;
	
	protected static Transaction transaction;
	
	@BeforeEach
	public void setUpBeforeEach() {
		em = emf.createEntityManager();
		session = em.unwrap(Session.class);
		transaction = session.getTransaction();
	}
	
	@AfterEach
	public void tearDownAfterEach() {
		if (em.isOpen()) {
			em.clear();
			em.close();
		}
		if (session.isOpen()) {
			session.clear();
			session.close();
		}
	}
	
}
