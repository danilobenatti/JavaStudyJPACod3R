package util;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Order;
import model.OrderItem;
import model.OrderItemPk;
import model.Person;
import model.Product;
import model.enums.OrderStatus;

public class NewOrder {
	
	public static void main(String[] args) {
		
		Transaction transaction = null;
		
		try (EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("javastudyjpa");
				EntityManager em = emf.createEntityManager();
				Session session = em.unwrap(Session.class)) {
			/**
			 * em.getTransaction().begin();
			 */
			transaction = session.getTransaction();
			transaction.begin();
			
			Person person = session.find(Person.class, 1L);
			
			Order order = Order.of(Date.from(Instant.now()), person, 0.1F,
					OrderStatus.WAITING);
			
			Product p1 = session.find(Product.class, 1L);
			OrderItem item1 = OrderItem.of(new OrderItemPk(), order, p1, 1);
			
			Product p2 = session.find(Product.class, 2L);
			OrderItem item2 = OrderItem.of(new OrderItemPk(), order, p2, 1);
			
			Product p3 = session.find(Product.class, 3L);
			OrderItem item3 = OrderItem.of(new OrderItemPk(), order, p3, 1);
			
			order.setOrderitems(Arrays.asList(item1, item2, item3));
			
			session.persist(order);
			
			transaction.commit();
			
			session.clear();
			
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw e;
		}
		
	}
	
}
