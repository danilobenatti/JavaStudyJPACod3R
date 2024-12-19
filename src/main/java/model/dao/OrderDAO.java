package model.dao;

import infra.DAO;
import model.Order;

public class OrderDAO extends DAO<Order> {
	
	public OrderDAO() {
		super(Order.class);
	}
	
}
