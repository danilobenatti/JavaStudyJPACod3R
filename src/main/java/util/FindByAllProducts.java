package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import infra.DAO;
import model.Product;
import model.dao.ProductDAO;

public class FindByAllProducts {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Configurator.initialize(FindByAllProducts.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		DAO<Product> dao = new DAO<>(Product.class);
		
		dao.listAll().forEach(l -> log.info("{}", l));
		
		ProductDAO productDAO = new ProductDAO();
		List<Product> products = productDAO.listAll(3, 0);
		
		products.forEach(p -> log.info("{}", p));
		
		log.info(products.stream().map(Product::getUnitPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		
		log.info(products.stream().map(Product::getUnitPriceWithDiscount)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
		
		dao.end();
		productDAO.end();
		
	}
	
}
