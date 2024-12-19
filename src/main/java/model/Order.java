package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_orders", schema = "javastudyjpa")
public class Order extends BaseEntityLong implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "col_date")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "person_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk__orders__person_id"))
	private Person person;
	
	@OneToMany(mappedBy = "order",
		cascade = { CascadeType.PERSIST, CascadeType.MERGE,
				CascadeType.REMOVE },
		orphanRemoval = true, fetch = FetchType.EAGER)
	private List<OrderItem> orderitems = new ArrayList<>();
	
	@Column(name = "col_discount", nullable = false)
	private float discount = 0;
	
	@Setter(value = AccessLevel.NONE)
	@Column(name = "col_total", nullable = false)
	private BigDecimal total = BigDecimal.ZERO;
	
	@Column(name = "col_status", nullable = false)
	private Byte status = OrderStatus.WAITING.getCode();
	
	@Builder(builderMethodName = "orderBuilder")
	public static Order of(Date date, Person person, float discount,
			OrderStatus status) {
		Order order = new Order();
		order.setDate(date);
		order.setPerson(person);
		order.setDiscount(discount);
		order.setStatus(status);
		return order;
	}
	
	public void addItem(OrderItem item) {
		if (item != null)
			this.orderitems.add(item);
		setTotal();
	}
	
	public void addItems(OrderItem... items) {
		for (OrderItem item : items) {
			this.addItem(item);
		}
	}
	
	public void setTotal() {
		this.total = calcTotal(this.orderitems, this.discount);
	}
	
	static boolean isEmptyOrNull(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}
	
	static boolean isNotEmptyOrNull(Collection<?> collection) {
		return !isEmptyOrNull(collection);
	}
	
	public BigDecimal calcTotal(List<OrderItem> orderitems, float discount) {
		if (isNotEmptyOrNull(orderitems)) {
			return orderitems.stream().map(OrderItem::calcSubTotal)
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.multiply(BigDecimal.valueOf(1 - discount));
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal calcTotal() {
		return calcTotal(this.orderitems, this.discount);
	}
	
	public OrderStatus getStatus() {
		return OrderStatus.toEnum(this.status);
	}
	
	public void setStatus(OrderStatus status) {
		if (status != null && !this.status.equals(status.getCode())) {
			this.status = status.getCode();
			if (status.equals(OrderStatus.PAID)) {
				getOrderItems().forEach(OrderItem::setSubTotal);
				this.setTotal();
			}
		}
	}
	
	public boolean isPaid() {
		return getStatus().equals(OrderStatus.PAID);
	}
	
	public boolean isWaitting() {
		return getStatus().equals(OrderStatus.WAITING);
	}
	
	public boolean isCanceled() {
		return getStatus().equals(OrderStatus.CANCELED);
	}
	
	public List<OrderItem> getOrderItems() {
		return Collections.unmodifiableList(this.orderitems);
	}
	
	@PrePersist
	private void whenPersist() {
		getOrderItems().forEach(OrderItem::setSubTotal);
		this.setTotal();
	}
	
	@PreUpdate
	private void whenUpdate() {
		if (isWaitting()) {
			getOrderItems().forEach(OrderItem::setSubTotal);
			this.setTotal();
		}
	}
	
}
