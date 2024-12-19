package model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@MappedSuperclass
public class BaseEntityLong implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Setter(value = AccessLevel.NONE)
	@Column(name = "col_dateinsert", columnDefinition = "timestamp",
		updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@org.hibernate.annotations.CreationTimestamp
	private LocalDateTime dateCreate;
	
	@Setter(value = AccessLevel.NONE)
	@Column(name = "col_dateupdate", columnDefinition = "timestamp",
		insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@org.hibernate.annotations.UpdateTimestamp
	private LocalDateTime dateUpdate;
	
	/**
	 * @jakarta.persistence.PrePersist
	 *  public void onPrePersist() {
	 *    this.dateCreate(LocalDateTime.now());
	 *    this.dateUpdate(LocalDateTime.now());
	 *  }
	 * 
	 * @jakarta.persistence.PreUpdate
	 *  public void onPreUpdate() {
	 *    this.dateUpdate(LocalDateTime.now());
	 *  }
	 **/
	
}
