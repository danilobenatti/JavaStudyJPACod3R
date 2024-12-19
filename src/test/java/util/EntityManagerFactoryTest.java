package util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@SuppressWarnings("all")
public class EntityManagerFactoryTest {
	
	protected static EntityManagerFactory emf;
	
	@BeforeAll
	public static void setUpBeforeAll() {
		emf = Persistence.createEntityManagerFactory("javastudyjpa");
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		if (emf.isOpen()) {
			emf.close();
		}
	}
	
}
