package util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import model.Product;
import model.dao.ProductDAO;

public class FindByProduct {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Configurator.initialize(FindByProduct.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		ProductDAO dao = new ProductDAO();
		
		Product product = dao.findById(2L);
		
		log.log(Level.INFO, "{}", product);
		
		dao.end();
		
	}
	
}
