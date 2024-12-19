package infra;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class DAO<E> {
	
	static final int BATCH_SIZE = 10;
	
	static Logger log = LogManager.getLogger();
	
	static EntityManagerFactory emf;
	
	static EntityManager em;
	
	Session session;
	
	Transaction transaction;
	
	private Class<E> entity;
	
	static {
		Configurator.initialize(DAO.class.getName(),
				"./src/main/resources/log4j2.properties");
		try {
			emf = Persistence.createEntityManagerFactory("javastudyjpa");
			em = emf.createEntityManager();
		} catch (Exception e) {
			log.error("Message error: %s", e.getMessage());
		}
	}
	
	public DAO() {
		this(null);
	}
	
	public DAO(Class<E> entity) {
		this.entity = entity;
		session = em.unwrap(Session.class);
		session.setJdbcBatchSize(BATCH_SIZE);
		transaction = session.getTransaction();
	}
	
	public DAO<E> beginTransaction() {
		transaction.begin();
		return this;
	}
	
	public DAO<E> commitTransaction() {
		transaction.commit();
		return this;
	}
	
	public DAO<E> add(E e) {
		session.persist(e);
		return this;
	}
	
	public DAO<E> update(E e) {
		session.merge(e);
		return this;
	}
	
	public DAO<E> delete(E e) {
		session.remove(e);
		return this;
	}
	
	public DAO<E> addEntity(E e) {
		return this.beginTransaction().add(e).commitTransaction();
	}
	
	public DAO<E> updateEntity(E e) {
		return this.beginTransaction().update(e).commitTransaction();
	}
	
	public DAO<E> deleteEntity(E e) {
		return this.beginTransaction().delete(e).commitTransaction();
	}
	
	public E searchById(Object id) {
		if (this.entity == null)
			throw new UnsupportedOperationException("Null class");
		String jpql = "select e from " + this.entity.getName()
				+ " e where e.id=" + id;
		return session.createQuery(jpql, this.entity).getSingleResult();
	}
	
	public E findById(Object id) {
		if (this.entity == null) {
			throw new UnsupportedOperationException("Null class");
		}
		return session.find(this.entity, id);
	}
	
	public List<E> listAll() {
		return this.listAll(5, 0);
	}
	
	public List<E> listAll(int maxResult, int startPosition) {
		if (this.entity == null)
			throw new UnsupportedOperationException("Null class");
		String jpql = "select e from " + this.entity.getName() + " e";
		TypedQuery<E> query = session.createQuery(jpql, this.entity);
		query.setMaxResults(maxResult);
		query.setFirstResult(startPosition);
		return query.getResultList();
	}
	
	public List<E> execute(String qlString, Object... objects) {
		TypedQuery<E> query = session.createNamedQuery(qlString, this.entity);
		int i = 1;
		for (Object object : objects) {
			query.setParameter(i++, object);
		}
		return query.getResultList();
	}
	
	public void end() {
		if (em.isOpen()) {
			em.clear();
			em.close();
		}
		if (session.isOpen()) {
			session.clear();
			session.close();
		}
	}
	
}
