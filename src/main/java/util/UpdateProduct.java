package util;

import model.Product;
import model.dao.ProductDAO;

public class UpdateProduct {
	
	public static void main(String[] args) {
		
		ProductDAO productDAO = new ProductDAO();
		Product product = productDAO.findById(1L);
		product.setDiscount(0.1F);
		
		productDAO.updateEntity(product);
		
		productDAO.end();
		
	}
	
}
