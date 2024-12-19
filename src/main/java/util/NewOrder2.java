package util;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Date.from;
import static model.enums.OrderStatus.PAID;
import static model.enums.OrderStatus.WAITING;

import java.time.Instant;

import infra.DAO;
import model.Order;
import model.OrderItem;
import model.OrderItemPk;
import model.Person;
import model.Product;
import model.dao.OrderDAO;
import model.dao.PersonDAO;
import model.dao.ProductDAO;

public class NewOrder2 {
	
	public static void main(String[] args) {
		
		DAO<Person> personDAO = new PersonDAO();
		
		Person p1 = personDAO.findById(1);
		Person p2 = personDAO.findById(2);
		Person p3 = personDAO.findById(3);
		Person p4 = personDAO.findById(4);
		
		Instant now = Instant.now();
		
		Order order1 = Order.of(from(now), p1, 0.1F, WAITING);
		Order order2 = Order.of(from(now.minus(15, DAYS)), p2, 0.05F, PAID);
		Order order3 = Order.of(from(now.minus(30, DAYS)), p3, 0, PAID);
		Order order4 = Order.of(from(now.minus(60, DAYS)), p3, 0.08F, PAID);
		Order order5 = Order.of(from(now.minus(30, DAYS)), p4, 0, WAITING);
		
		DAO<Product> productDAO = new ProductDAO();
		
		Product prod1 = productDAO.findById(1);
		Product prod2 = productDAO.findById(2);
		Product prod3 = productDAO.findById(3);
		Product prod4 = productDAO.findById(4);
		
		OrderItem item1 = OrderItem.of(new OrderItemPk(), order1, prod1, 1);
		OrderItem item2 = OrderItem.of(new OrderItemPk(), order1, prod2, 1);
		OrderItem item3 = OrderItem.of(new OrderItemPk(), order1, prod3, 1);
		
		OrderItem item4 = OrderItem.of(new OrderItemPk(), order2, prod1, 2);
		OrderItem item5 = OrderItem.of(new OrderItemPk(), order2, prod2, 1);
		
		OrderItem item6 = OrderItem.of(new OrderItemPk(), order3, prod4, 2);
		
		OrderItem item7 = OrderItem.of(new OrderItemPk(), order4, prod3, 2);
		
		OrderItem item8 = OrderItem.of(new OrderItemPk(), order5, prod1, 1);
		OrderItem item9 = OrderItem.of(new OrderItemPk(), order5, prod2, 2);
		
		order1.addItems(item1, item2, item3);
		
		order2.addItems(item4, item5);
		
		order3.addItem(item6);
		
		order4.addItem(item7);
		
		order5.addItems(item8, item9);
		
		OrderDAO orderDAO = new OrderDAO();
		
		orderDAO.addEntity(order1);
		orderDAO.addEntity(order2);
		orderDAO.addEntity(order3);
		orderDAO.addEntity(order4);
		orderDAO.addEntity(order5);
		
		orderDAO.end();
		productDAO.end();
		personDAO.end();
		
	}
	
}
