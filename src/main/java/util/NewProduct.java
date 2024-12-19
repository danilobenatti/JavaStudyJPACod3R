package util;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import model.Product;
import model.dao.ProductDAO;
import model.enums.ProductUnit;

public class NewProduct {
	
	public static void main(String[] args) {
		
		ProductDAO dao = new ProductDAO();
		
		Product p1 = Product.of("Ballpoint Pen 0.5", "0.5mm blue color", 0.05F,
				BigDecimal.valueOf(3.5), ProductUnit.UNITY);
		
		Product p2 = Product.of("Ballpoint Pen 0.7", "0.7mm black color", 0.05F,
				BigDecimal.valueOf(3.5), ProductUnit.UNITY);
		
		Product p3 = Product.of("Craft glue 100ml",
				"Glue for school activities", 0, BigDecimal.valueOf(6),
				ProductUnit.UNITY);
		
		Product p4 = Product.of("Product Test", "Description Test", 0.1F,
				BigDecimal.valueOf(5.0), ProductUnit.KILOGRAM);
		
		dao.addEntity(p1.setValidity(2, ChronoUnit.YEARS));
		dao.addEntity(p2.setValidity(2, ChronoUnit.YEARS));
		dao.addEntity(p3.setValidity(1, ChronoUnit.YEARS));
		dao.addEntity(p4.setValidity(6, ChronoUnit.MONTHS));
		
		dao.end();
		
	}
	
}
