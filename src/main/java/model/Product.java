package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.enums.ProductUnit;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_products", schema = "javastudyjpa")
public class Product extends BaseEntityLong implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "col_title", length = 150, nullable = false)
	private String title;
	
	@Column(name = "col_description", length = 255, nullable = false)
	private String description;
	
	@Column(name = "col_discount")
	private float discount = 0;
	
	@Column(name = "col_unitprice", precision = 10, scale = 2, nullable = false)
	private BigDecimal unitPrice = BigDecimal.ZERO;
	
	@Column(name = "col_unit", nullable = false)
	private Byte unit = ProductUnit.UNITY.getCode();
	
	@Column(name = "col_validity")
	private LocalDate validity;
	
	@OneToMany(mappedBy = "product")
	private List<OrderItem> orderItems = new ArrayList<>();
	
	public ProductUnit getUnit() {
		return ProductUnit.toEnum(this.unit);
	}
	
	public void setUnit(ProductUnit unit) {
		this.unit = unit.getCode();
		
	}
	
	@Builder(builderMethodName = "productBuilder")
	public static Product of(String title, String description, float discount,
			BigDecimal unitPrice, ProductUnit unit) {
		Product product = new Product();
		product.setTitle(title);
		product.setDescription(description);
		product.setDiscount(discount);
		product.setUnitPrice(unitPrice);
		product.setUnit(unit);
		return product;
	}
	
	public BigDecimal getUnitPriceWithDiscount() {
		return getUnitPrice().multiply(BigDecimal.valueOf(1 - getDiscount()));
	}
	
	public Product setValidity(long i, TemporalUnit unit) {
		LocalDate date = getDateCreate() != null ? getDateCreate().toLocalDate()
				: LocalDate.now(ZoneId.systemDefault());
		setValidity(switch (unit) {
			case ChronoUnit.DAYS -> date.plusDays(i);
			case ChronoUnit.WEEKS -> date.plusWeeks(i);
			case ChronoUnit.MONTHS -> date.plusMonths(i);
			case ChronoUnit.YEARS -> date.plusYears(i);
			default ->
				throw new IllegalArgumentException("Unexpected value: " + unit);
		});
		return this;
	}
	
	public Product setValidity(long i, TemporalUnit unit, ZoneId zoneId) {
		LocalDate date = getDateCreate() != null ? getDateCreate().toLocalDate()
				: LocalDate.now(zoneId);
		setValidity(switch (unit) {
			case ChronoUnit.DAYS -> date.plusDays(i);
			case ChronoUnit.WEEKS -> date.plusWeeks(i);
			case ChronoUnit.MONTHS -> date.plusMonths(i);
			case ChronoUnit.YEARS -> date.plusYears(i);
			default ->
				throw new IllegalArgumentException("Unexpected value: " + unit);
		});
		return this;
	}
	
	public boolean validityIsOk() {
		ZoneId zoneId = ZoneId.systemDefault();
		return getValidity().compareTo(LocalDate.now(zoneId)) > 0;
	}
	
	public boolean validityIsOk(ZoneId zoneId) {
		return getValidity().compareTo(LocalDate.now(zoneId)) > 0;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [");
		builder.append("id=");
		builder.append(getId());
		builder.append(", title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", discount=");
		builder.append(discount);
		builder.append(", unitPrice=");
		builder.append(unitPrice);
		builder.append(", unit=");
		builder.append(ProductUnit.toEnum(unit).getValue());
		builder.append(", validity=");
		builder.append(validity);
		builder.append(", dateCreate=");
		builder.append(getDateCreate());
		builder.append(", dateUpdate=");
		builder.append(getDateUpdate());
		builder.append("]");
		return builder.toString();
	}
	
}
