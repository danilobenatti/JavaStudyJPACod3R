package util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import infra.DAO;
import model.Address;
import model.Person;
import model.dao.PersonDAO;

public class FindByPerson {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Configurator.initialize(FindByPerson.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		PersonDAO personDAO = new PersonDAO();
		
		Person person = personDAO.findById(1L);
		
		log.info("Find: {}", person);
		
		DAO<Address> addressDAO = new DAO<>(Address.class);
		
		Address address = addressDAO.findById(1L);
		
		log.info("Find: {}", address.getPerson());
		
		List<Person> find = personDAO.execute("Person.findByName", "ON");
		find.forEach(log::info);
		
		List<Person> list = personDAO.execute("Persons.listByOrders", "ON", 10);
		list.forEach(log::info);
		
		personDAO.end();
		addressDAO.end();
		
	}
	
}
