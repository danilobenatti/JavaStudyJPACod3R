package util;

import static model.Address.NewAddress;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Address;
import model.Person;

public class NewAddressBatch {
	
	static final int BATCH_SIZE = 10;
	
	public static void main(String[] args) {
		
		Transaction transaction = null;
		
		try (EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
				EntityManager em = emf.createEntityManager();
				Session session = em.unwrap(Session.class)) {
			
			session.setJdbcBatchSize(BATCH_SIZE);
			
			Person p1 = session.find(Person.class, 1L);
			Person p2 = session.find(Person.class, 2L);
			Person p3 = session.find(Person.class, 3L);
			
			Address a1 = Address.of(p1, "Street One Way", "456789");
			Address a2 = Address.of(p2, "Parkway St8", "123456");
			Address a3 = Address.of(p3, "18th St", "32805");
			Address a4 = NewAddress().street("Seven Seas Drive")
					.zipCode("32836").build();
			Address a5 = NewAddress().street("Floridian Way").zipCode("34787")
					.build();
			Address a6 = NewAddress().street("W Buena Vista Dr")
					.zipCode("34747").build();
			Address a7 = NewAddress().street("Century Dr").zipCode("34747")
					.build();
			
			List<Address> addresses = Arrays.asList(a1, a2, a3, a4, a5, a6, a7);
			
			p1.setAddress(a1);
			p2.setAddress(a2);
			p3.setAddress(a3);
			
			transaction = session.beginTransaction();
			
			int i = 0;
			for (Address address : addresses) {
				if (++i % BATCH_SIZE == 0) {
					session.flush();
					session.clear();
				}
				session.persist(address);
			}
			
			List<Person> persons = Arrays.asList(p1, p2, p3);
			
			for (Person person : persons) {
				session.merge(person);
			}
			
			transaction.commit();
			
			session.clear();
			
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		}
		
	}
	
}
