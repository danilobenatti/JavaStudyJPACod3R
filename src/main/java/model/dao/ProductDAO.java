package model.dao;

import infra.DAO;
import model.Product;

public class ProductDAO extends DAO<Product> {
	
	public ProductDAO() {
		super(Product.class);
	}
	
}
