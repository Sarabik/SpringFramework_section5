package com.springframework.section5.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "beer_order")
public class BeerOrder {

	public BeerOrder(
		final UUID id,
		final LocalDateTime createdDate,
		final LocalDateTime lastModifiedDate,
		final Integer version,
		final String customerRef,
		final Customer customer,
		final BeerOrderShipment beerOrderShipment,
		final Set<BeerOrderLine> beerOrderLines
	) {
		this.id = id;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.version = version;
		this.customerRef = customerRef;
		this.setCustomer(customer);
		this.setBeerOrderShipment(beerOrderShipment);
		this.beerOrderLines = beerOrderLines;
	}

	@Id
	@GeneratedValue
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@UpdateTimestamp
	private LocalDateTime lastModifiedDate;

	@Version
	private Integer version;

	@Size(max = 255)
	@NotNull
	private String customerRef;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@OneToOne(mappedBy = "beerOrder", cascade = CascadeType.PERSIST)
	private BeerOrderShipment beerOrderShipment;

	public void setBeerOrderShipment(final BeerOrderShipment beerOrderShipment) {
		this.beerOrderShipment = beerOrderShipment;
		beerOrderShipment.setBeerOrder(this);
	}

	@OneToMany(mappedBy = "beerOrder", fetch = FetchType.LAZY)
	@ToString.Exclude
	@Builder.Default
	private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public final boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof final BeerOrder beerOrder)) {
			return false;
		}

		return Objects.equals(id, beerOrder.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
		customer.getBeerOrders().add(this);
	}
}