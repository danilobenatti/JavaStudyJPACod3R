package util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Person;

public class FindByAllPersons {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Configurator.initialize(FindByAllPersons.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
		EntityManager em = emf.createEntityManager();
		
		try {
			
			String jpql = "select p from Person p";
			TypedQuery<Person> query = em.createQuery(jpql, Person.class);
			query.setMaxResults(3);
			
			List<Person> list = query.getResultList();
			
			list.forEach(person -> log.info("{}", person));
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			em.close();
			emf.close();
			
		}
		
	}
	
}
