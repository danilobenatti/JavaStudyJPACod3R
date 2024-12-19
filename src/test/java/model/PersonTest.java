package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.persistence.TypedQuery;
import util.EntityManagerTest;

class PersonTest extends EntityManagerTest {
	
	@ParameterizedTest
	@CsvSource(delimiterString = ";",
		value = { "Jony; M; 88.7; 1.82; 1990-05-15",
				"Paul; M; 91.1; 1.89; 1985-11-07",
				"Betty; F; 54.6; 1.59; 1987-09-26",
				"Person Test; M; 80; 1.8; 1995-01-01" })
	void insertPersonsTest(String firstname, char gender, float weight,
			float height, LocalDate birthday) {
		
		Person person = Person.of(firstname, gender, weight, height, birthday);
		
		session.getTransaction().begin();
		session.persist(person);
		session.getTransaction().commit();
		
		assertFalse(session.getTransaction().isActive());
		assertTrue(session.getEntityManagerFactory().isOpen());
	}
	
	@Test
	void searchPersonTest() {
		Person person = session.find(Person.class, 2L);
		System.out.println(person);
		assertEquals("Paul", person.getFirstname());
	}
	
	@Test
	void listAllPersonsTest() {
		
		TypedQuery<Person> query = session.createQuery("select p from Person p",
				Person.class);
		query.setMaxResults(3);
		
		List<Person> resultList = query.getResultList();
		resultList.forEach(System.out::println);
		
		assertEquals(3, resultList.size());
	}
	
	@Test
	void updatePersonTest1() {
		Person person = session.find(Person.class, 2L);
		person.setFirstname("Paul Update 1");
		person.setWeight(85.5F);
		session.getTransaction().begin();
		session.merge(person);
		session.getTransaction().commit();
		Person updatePerson = session.find(Person.class, 2L);
		assertEquals("Paul Update 1", updatePerson.getFirstname());
		assertEquals(85.5F, updatePerson.getWeight());
	}
	
	@Test
	void updatePersonTest2() {
		Person person = session.find(Person.class, 2L);
		person.setFirstname("Paul Update 2");
		person.setWeight(75.5F);
		person.diedPersonAt(LocalDate.of(2023, Month.JANUARY, 20));
		session.detach(person);
		session.getTransaction().begin();
		session.merge(person);
		session.getTransaction().commit();
		Person updatePerson = session.find(Person.class, 2L);
		assertEquals("Paul Update 2", updatePerson.getFirstname());
		assertEquals(75.5F, updatePerson.getWeight());
		assertEquals(37, updatePerson.getAge());
	}
	
	@Test
	void deletePersonTest() {
		Person person = session.find(Person.class, 4L);
		if (person != null) {
			session.getTransaction().begin();
			session.remove(person);
			session.getTransaction().commit();
		}
		assertNull(session.find(Person.class, 4L));
	}
}
