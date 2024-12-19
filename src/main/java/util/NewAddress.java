package util;

import java.util.Arrays;
import java.util.List;

import infra.DAO;
import model.Address;
import model.Person;
import model.dao.PersonDAO;

public class NewAddress {
	
	public static void main(String[] args) {
		
		PersonDAO personDAO = new PersonDAO();
		DAO<Address> addressDAO = new DAO<>(Address.class);
		
		Person p1 = personDAO.findById(1L);
		Person p2 = personDAO.findById(2L);
		Person p3 = personDAO.findById(3L);
		
		Address a1 = Address.of(p1, "Street One Way", "456789");
		Address a2 = Address.of(p2, "Parkway St8", "123456");
		Address a3 = Address.of(p3, "18th St", "32805");
		Address a4 = Address.of(null, "Seven Seas Drive", "32836");
		Address a5 = Address.of(null, "Floridian Way", "34787");
		Address a6 = Address.of(null, "W Buena Vista Dr", "34747");
		Address a7 = Address.of(null, "Century Dr", "34747");
		
		List<Address> addresses = Arrays.asList(a1, a2, a3, a4, a5, a6, a7);
		
		p1.setAddress(a1);
		p2.setAddress(a2);
		p3.setAddress(a3);
		
		addressDAO.beginTransaction();
		for (Address address : addresses) {
			addressDAO.add(address);
		}
		addressDAO.commitTransaction();
		
		addressDAO.end();
		personDAO.end();
		
	}
	
}
