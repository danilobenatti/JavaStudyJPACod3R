package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import infra.DAO;
import model.dao.ProductDAO;
import model.enums.ProductUnit;

class ProductTest {
	
	@Test
	void insertProductDaoTest() {
		DAO<Product> dao = new ProductDAO();
		
		Product p1 = new Product();
		p1.setTitle("Ballpoint Pen 0.5");
		p1.setDescription("0.5mm blue color");
		p1.setDiscount(0.05F);
		p1.setUnitPrice(BigDecimal.valueOf(3.5));
		p1.setUnit(ProductUnit.UNITY);
		p1.setValidity(2, ChronoUnit.YEARS);
		
		Product p2 = Product.productBuilder().title("Ballpoint Pen 0.7")
				.description("0.7mm black color")
				.unitPrice(BigDecimal.valueOf(3.5)).discount(0.05f)
				.unit(ProductUnit.UNITY).build();
		
		Product p3 = Product.of("Craft glue 100ml",
				"Glue for school activities", 0, BigDecimal.valueOf(6),
				ProductUnit.UNITY).setValidity(1, ChronoUnit.YEARS);
		
		Product p4 = Product.of("Product Test", "Description Test", 0.1F,
				BigDecimal.valueOf(5.0), ProductUnit.KILOGRAM);
		p4.setValidity(6, ChronoUnit.MONTHS);
		
		dao.addEntity(p1);
		dao.addEntity(p2.setValidity(24, ChronoUnit.MONTHS));
		dao.addEntity(p3.setValidity(1, ChronoUnit.YEARS));
		dao.addEntity(p4);
		
		List<Product> products = dao.listAll();
		products.forEach(System.out::println);
		
		assertEquals(4, products.size());
		
		dao.end();
	}
	
	@Test
	void listAllProductDaoTest() {
		ProductDAO productDAO = new ProductDAO();
		
		List<Product> products = productDAO.listAll();
		
		double total = products.stream().map(Product::getUnitPriceWithDiscount)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
		
		System.out.println(String.format("Total: %s", DecimalFormat
				.getCurrencyInstance(Locale.of("pt", "BR")).format(total)));
		
		assertEquals(17.15, total);
		
		productDAO.end();
	}
	
	@Test
	void updateProductDaoTest() {
		ProductDAO productDAO = new ProductDAO();
		Product product = productDAO.findById(1L);
		
		product.setDescription("0.5mm blue color Especial Edition");
		product.setUnitPrice(BigDecimal.valueOf(6.5));
		product.setDiscount(0F);
		
		productDAO.updateEntity(product);
		Assertions.assertEquals(BigDecimal.valueOf(6.5).setScale(2),
				productDAO.findById(1L).getUnitPriceWithDiscount());
		productDAO.end();
	}
	
}
