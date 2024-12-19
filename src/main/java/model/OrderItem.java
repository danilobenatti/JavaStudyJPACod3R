package model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_orderitems", schema = "javastudyjpa")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private OrderItemPk id;
	
	@NotNull
	@MapsId(value = "orderId")
	@ManyToOne(optional = false, cascade = CascadeType.ALL,
		fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", nullable = false,
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name = "fk__orderitem__order_id"))
	private Order order;
	
	@NotNull
	@MapsId(value = "productId")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false,
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name = "fk__orderitem__product_id"))
	private Product product;
	
	@Column(name = "col_quantity", nullable = false)
	private double quantity;
	
	@Setter(value = AccessLevel.NONE)
	@Column(name = "col_subtotal", nullable = false)
	private BigDecimal subtotal;
	
	@Builder(builderMethodName = "itemBuilder")
	public static OrderItem of(OrderItemPk id, Order order, Product product,
			double quantity) {
		OrderItem item = new OrderItem();
		item.setId(id);
		item.setOrder(order);
		item.setProduct(product);
		item.setQuantity(quantity);
		return item;
	}
	
	public void setSubTotal() {
		this.subtotal = calcSubTotal(this.product, this.quantity);
	}
	
	public BigDecimal calcSubTotal() {
		return calcSubTotal(this.product, this.quantity);
	}
	
	public BigDecimal calcSubTotal(Product product, double quantity) {
		BigDecimal productPrice = product.getUnitPriceWithDiscount();
		if (productPrice.compareTo(BigDecimal.ZERO) > 0
				&& Double.compare(quantity, 0) > 0) {
			return productPrice.multiply(BigDecimal.valueOf(quantity));
		}
		return BigDecimal.ZERO;
	}
	
	@PrePersist
	public void whenPersist() {
		this.setSubTotal();
	}
	
	@PreUpdate
	public void whenUpdate() {
		if (getOrder().isWaitting()) {
			this.setSubTotal();
		}
	}
	
}
