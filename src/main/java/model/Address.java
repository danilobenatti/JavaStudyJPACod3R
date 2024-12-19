package model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tbl_addresses", schema = "javastudyjpa")
public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "col_streetname", length = 255)
	private String street;
	
	@Column(name = "col_zipcode", length = 10)
	private String zipCode;
	
	@OneToOne(mappedBy = "address", fetch = FetchType.LAZY,
		orphanRemoval = true)
	private Person person;
	
	@Builder(builderMethodName = "NewAddress")
	public static Address of(Person person, String street, String zipCode) {
		Address address = new Address();
		address.setPerson(person);
		address.setStreet(street);
		address.setZipCode(zipCode);
		return address;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[street=");
		builder.append(street);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append("]");
		return builder.toString();
	}
	
}
