package model.dao;

import infra.DAO;
import model.Person;

public class PersonDAO extends DAO<Person> {
	
	public PersonDAO() {
		super(Person.class);
	}
	
}
