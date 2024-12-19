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

public class UpdatePerson1 {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Transaction transaction = null;
		
		Configurator.initialize(UpdatePerson1.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		try (EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
				EntityManager em = emf.createEntityManager();
				Session session = em.unwrap(Session.class)) {
			
			transaction = session.beginTransaction();
			
			Person person = session.find(Person.class, 1L);
			person.setFirstname("Jony Bill");
			person.setDeathDate(LocalDate.now());
			
			Person personUpt = session.merge(person);
			
			transaction.commit();
			
			log.info(personUpt);
			
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
