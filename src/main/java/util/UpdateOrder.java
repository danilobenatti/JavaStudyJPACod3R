package util;

import model.Order;
import model.Product;
import model.dao.OrderDAO;
import model.dao.ProductDAO;
import model.enums.OrderStatus;

public class UpdateOrder {
	
	public static void main(String[] args) {
		
		ProductDAO productDAO = new ProductDAO();
		Product product = productDAO.findById(1L);
		product.setDiscount(0.1F);
		productDAO.updateEntity(product);
		
		OrderDAO orderDAO = new OrderDAO();
		Order order = orderDAO.findById(1L);
		order.setDiscount(0);
		order.setStatus(OrderStatus.PAID);
		
		orderDAO.updateEntity(order);
		
		productDAO.end();
		orderDAO.end();
		
	}
	
}
