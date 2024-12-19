package util;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.Address;
import model.Person;

public class NewPersons {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Transaction transaction = null;
		
		Configurator.initialize(NewPersons.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		try (EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
				EntityManager em = emf.createEntityManager();
				Session session = em.unwrap(Session.class)) {
			
			transaction = session.getTransaction();
			
			Person p1 = Person.of("Jony", 'M', 88.7F, 1.82F,
					LocalDate.of(1990, Month.MAY, 15));
			p1.addPhone('M', "(077)97777-5555");
			
			Person p2 = Person.of("Paul", 'M', 91.1F, 1.89F,
					LocalDate.of(1985, Month.FEBRUARY, 7));
			p2.addPhone('H', "(045)7878-4545");
			
			Person p3 = Person.of("Betty", 'F', 54.6F, 1.59F,
					LocalDate.of(1987, Month.SEPTEMBER, 26));
			p3.addPhones(Map.of('W', "(015)4545-5050", 'M', "(021)98787-8989"));
			
			Person pTst = Person.of("Person Test", 'M', 80F, 1.8F,
					LocalDate.of(1995, Month.JANUARY, 1));
			pTst.setAddress(Address.of(pTst, "Street Test", "000000"));
			pTst.addPhones(Map.ofEntries(Map.entry('H', "(019)3545-4545"),
					Map.entry('M', "(019)99889-7788"),
					Map.entry('W', "(014)3565-5212")));
			
			List<Person> persons = Arrays.asList(p1, p2, p3, pTst);
			
			transaction.begin();
			
			for (Person person : persons) {
				session.persist(person);
			}
			transaction.commit();
			
			session.clear();
			
			TypedQuery<Person> typedQuery = session.createQuery(
					"select p from Person p left join Address a on p.address.id = a.id",
					Person.class);
			List<Person> list = typedQuery.getResultList();
			
			list.forEach(p -> log.info("Add: {}", p));
			
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		}
		
	}
	
}
