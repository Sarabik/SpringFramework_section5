package com.springframework.section5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "beer_order_line")
public class BeerOrderLine {

	public BeerOrderLine(
		final UUID id,
		final BeerOrder beerOrder,
		final Beer beer,
		final LocalDateTime createdDate,
		final LocalDateTime lastModifiedDate,
		final Integer orderQuantity,
		final Integer quantityAllocated,
		final Integer version
	) {
		this.id = id;
		this.setBeerOrder(beerOrder);
		this.beer = beer;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.orderQuantity = orderQuantity;
		this.quantityAllocated = quantityAllocated;
		this.version = version;
	}

	@Id
	@GeneratedValue
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "beer_order_id", nullable = false)
	private BeerOrder beerOrder;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "beer_id", nullable = false)
	private Beer beer;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@UpdateTimestamp
	private LocalDateTime lastModifiedDate;

	private Integer orderQuantity;

	private Integer quantityAllocated;

	@Version
	private Integer version;

	public void setBeerOrder(final BeerOrder beerOrder) {
		this.beerOrder = beerOrder;
		beerOrder.getBeerOrderLines().add(this);
	}

	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public final boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof final BeerOrderLine that)) {
			return false;
		}

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}