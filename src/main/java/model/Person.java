package model;

import static java.time.Instant.now;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_persons", schema = "javastudyjpa",
	uniqueConstraints = @UniqueConstraint(name = "uk__persons__address_id",
		columnNames = { "address_id" }))
@NamedQueries(@NamedQuery(name = "Person.findByName",
	query = "select p from Person p where p.firstname like concat('%', ?1, '%')"))
public class Person extends BaseEntityLong implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "col_firstname", nullable = false, length = 150)
	private String firstname;
	
	@Column(name = "col_gender", nullable = true)
	private Character gender;
	
	@Column(name = "col_weight", nullable = true)
	private float weight;
	
	@Column(name = "col_height", nullable = true)
	private float height;
	
	@Column(name = "col_birthday", nullable = false)
	private LocalDate birthDate;
	
	@Column(name = "col_deathday")
	private LocalDate deathDate = null;
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL,
		fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id",
		foreignKey = @ForeignKey(name = "fk__persons__address_id"))
	private Address address;
	
	@Setter(value = AccessLevel.NONE)
	@ElementCollection
	@CollectionTable(name = "tbl_persons_phones", schema = "javastudyjpa",
		joinColumns = @JoinColumn(name = "person_id", table = "tbl_persons",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk__persons_phones__person_id")))
	@MapKeyColumn(name = "col_type", columnDefinition = "char(1)",
		nullable = false)
	@Column(name = "col_number", length = 20, nullable = false)
	private Map<Character, String> phones = new HashMap<>();
	
	public void addPhone(Character type, String number) {
		this.phones.put(type, number);
	}
	
	public void addPhones(Map<Character, String> phones) {
		this.phones.putAll(phones);
	}
	
	@OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();
	
	@Builder(builderMethodName = "personBuilder")
	public static Person of(String firstname, Character gender, float weight,
			float height, LocalDate birthDate) {
		Person person = new Person();
		person.setFirstname(firstname);
		person.setGender(gender);
		person.setWeight(weight);
		person.setHeight(height);
		person.setBirthDate(birthDate);
		person.setDeathDate(null);
		return person;
	}
	
	private static final ZoneId ZONE_ID = ZoneId.systemDefault();
	
	public boolean isAlive() {
		return getBirthDate() != null && getDeathDate() == null;
	}
	
	public void diedPersonNow() {
		if (isAlive())
			setDeathDate(now().atZone(ZONE_ID).toLocalDate());
	}
	
	public void diedPersonAt(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZONE_ID).toLocalDate();
		diedPersonAt(localDate);
	}
	
	public void diedPersonAt(Date date, ZoneId zoneId) {
		LocalDate localDate = date.toInstant().atZone(zoneId).toLocalDate();
		diedPersonAt(localDate);
	}
	
	public void diedPersonAt(LocalDate date) {
		if (isAlive() && date.compareTo(getBirthDate()) >= 0)
			setDeathDate(date);
	}
	
	public int getAge() {
		LocalDate now = LocalDate.now();
		if (isAlive() && getBirthDate().isBefore(now)) {
			return Period.between(getBirthDate(), now).getYears();
		} else if (!isAlive() && getDeathDate().isAfter(this.birthDate)) {
			return Period.between(getBirthDate(), getDeathDate()).getYears();
		}
		return 0;
	}
	
	public String getAgeWithSymbol() {
		return isAlive()
				? String.valueOf(getAge()).concat(String.valueOf('\u2605'))
				: String.valueOf(getAge()).concat(String.valueOf('\u2020'));
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append(" [");
		builder.append("id=");
		builder.append(getId());
		builder.append(", firstname=");
		builder.append(firstname);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", weight=");
		builder.append(weight);
		builder.append(", height=");
		builder.append(height);
		builder.append(", age=");
		builder.append(getAgeWithSymbol());
		builder.append(", birthDate=");
		builder.append(birthDate);
		builder.append(", deathDate=");
		builder.append(deathDate);
		builder.append(", address=");
		builder.append(address);
		builder.append(", phones=");
		builder.append(getPhones());
		builder.append(", dateCreate=");
		builder.append(getDateCreate());
		builder.append(", dateUpdate=");
		builder.append(getDateUpdate());
		builder.append("]");
		return builder.toString();
	}
	
}
