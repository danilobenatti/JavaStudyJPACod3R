package model;

import static model.Address.NewAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import infra.DAO;
import model.dao.PersonDAO;

class AddressTest {
	
	@ParameterizedTest
	@CsvSource(delimiterString = ";", nullValues = "NULL",
		value = { "1; Street One Way; 456789", "2; Parkway St8; 123456",
				"3; 18th St; 32805" })
	void insertAddressesTest(long id, String street, String zipCode) {
		
		DAO<Object> dao = new DAO<>(Object.class);
		
		Address address = new Address();
		address.setStreet(street);
		address.setZipCode(zipCode);
		
		dao.addEntity(address).end();
		
		PersonDAO personDAO = new PersonDAO();
		
		Person person = personDAO.findById(id);
		person.setAddress(address);
		
		personDAO.updateEntity(person).end();
		assertEquals(street, person.getAddress().getStreet());
	}
	
	@ParameterizedTest
	@CsvSource(delimiterString = ";", nullValues = "NULL",
		value = { "1; Street One Way; 456789", "2; Parkway St8; 123456",
				"3; 18th St; 32805" })
	void cascadeInsertAddressesTest(long id, String street,
			String zipCode) {
		
		Address address = NewAddress().street(street).zipCode(zipCode)
				.build();
		
		PersonDAO personDAO = new PersonDAO();
		Person person = personDAO.findById(id);
		person.setAddress(address);
		
		personDAO.updateEntity(person).end();
		assertEquals(street, person.getAddress().getStreet());
	}
	
	@Test
	void findAddressByPerson() {
		
		DAO<Person> daoPerson = new DAO<>(Person.class);
		
		Person person = daoPerson.searchById(1L);
		System.out.println(person.getAddress().getStreet());
		daoPerson.end();
		assertEquals("Street One Way", person.getAddress().getStreet());
		
		DAO<Address> daoAddress = new DAO<>(Address.class);
		
		Address address = daoAddress.searchById(1L);
		System.out.println(address.getPerson().getFirstname());
		daoAddress.end();
		assertEquals("Jony", address.getPerson().getFirstname());
		
	}
}
