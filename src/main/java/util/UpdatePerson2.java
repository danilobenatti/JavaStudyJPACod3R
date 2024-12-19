package util;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Person;

public class UpdatePerson2 {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Transaction transaction = null;
		
		Configurator.initialize(UpdatePerson2.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		try (EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
				EntityManager em = emf.createEntityManager();
				Session session = em.unwrap(Session.class)) {
			
			transaction = session.beginTransaction();
			
			Person person = session.find(Person.class, 2L);
			person.setFirstname("Paul Grewbor");
			person.setDeathDate(LocalDate.now());
			
			transaction.commit();
			
			log.info(person);
			
			session.clear();
			em.clear();
			
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		}
		
	}
	
}
