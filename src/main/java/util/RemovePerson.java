package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.Session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Person;

public class RemovePerson {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Configurator.initialize(RemovePerson.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		try (EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
				EntityManager em = emf.createEntityManager();
				Session session = em.unwrap(Session.class)) {
			
			Person person = session.find(Person.class, 4L);
			
			if (person != null) {
				session.getTransaction().begin();
				session.remove(person);
				session.getTransaction().commit();
			}
			
		}
		
	}
	
}
