package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import infra.DAO;
import model.dao.OrderDAO;
import model.dao.PersonDAO;
import model.dao.ProductDAO;
import model.enums.OrderStatus;

class OrderTest {
	
	@Test
	void insertOrderAndItemsTest() {
		
		DAO<Person> personDAO = new PersonDAO();
		DAO<Product> productDAO = new ProductDAO();
		DAO<Order> orderDAO = new OrderDAO();
		
		Person person = personDAO.findById(1L);
		
		Order order = new Order();
		order.setDate(Date.from(Instant.now()));
		order.setDiscount(0);
		order.setStatus(OrderStatus.WAITING);
		order.setPerson(person);
		
		Product product1 = productDAO.findById(1L);
		OrderItem item1 = new OrderItem();
		item1.setId(new OrderItemPk());
		item1.setOrder(order);
		item1.setProduct(product1);
		item1.setQuantity(1);
		
		Product product2 = productDAO.findById(2L);
		OrderItem item2 = new OrderItem();
		item2.setId(new OrderItemPk());
		item2.setOrder(order);
		item2.setProduct(product2);
		item2.setQuantity(2);
		
		Product product3 = productDAO.findById(3L);
		OrderItem item3 = new OrderItem();
		item3.setId(new OrderItemPk());
		item3.setOrder(order);
		item3.setProduct(product3);
		item3.setQuantity(1);
		
		order.setOrderitems(Arrays.asList(item1, item2, item3));
		
		orderDAO.addEntity(order);
		
		OrderDAO daoTest = new OrderDAO();
		Order orderTest = daoTest.findById(1L);
		
		assertEquals(OrderStatus.WAITING, orderTest.getStatus());
		assertEquals(BigDecimal.valueOf(21.26).setScale(2),
				orderTest.getTotal().setScale(2, RoundingMode.HALF_EVEN));
		
		personDAO.end();
		productDAO.end();
		orderDAO.end();
		
		daoTest.end();
		
	}
	
	@Test
	void updateOrderTest() {
		DAO<Order> orderDAO = new DAO<>(Order.class);
		Order order = orderDAO.findById(1L);
		
//		OrderItem item = order.getOrderItems().stream()
//				.filter(i -> i.getId().getProductId() == 2L)
//				.collect(Collectors.toList()).removeFirst();
//		System.out.println(item.toString());
		
		Predicate<OrderItem> filter = i -> i.getProduct().getId() == 2L;
		List<OrderItem> list = order.getOrderItems().stream()
				.filter(filter.negate()).toList();
		// List<OrderItem> list =
		// order.getOrderItems().stream().filter(filter.negate()).collect(Collectors.toList());
		// List<OrderItem> list =
		// order.getOrderItems().stream().filter(filter.negate()).toList();
		order.setOrderitems(list);
		order.setStatus(OrderStatus.PAID);
		orderDAO.updateEntity(order).end();
//		
//		OrderDAO dao = new OrderDAO();
//		Order orderUpdate = dao.findById(1L);
//		System.out.println(orderUpdate.getTotal());
		
//		orderUpdate.setDiscount(0.5f);
//		orderUpdate.setTotal();
//		dao.updateEntity(order);
//		dao.end();
//		
//		OrderDAO daoAssert = new OrderDAO();
//		Order orderAssert = daoAssert.findById(1L);
//		assertEquals(BigDecimal.valueOf(25.60).setScale(2),
//				orderAssert.calcTotal().setScale(2, RoundingMode.HALF_EVEN));
		
	}
}
