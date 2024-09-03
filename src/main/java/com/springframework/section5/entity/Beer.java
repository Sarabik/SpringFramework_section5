package com.springframework.section5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Beer {

	public Beer(
		final UUID id,
		final Integer version,
		final String beerName,
		final BeerStyle beerStyle,
		final String upc,
		final Integer quantityOnHand,
		final BigDecimal price,
		final LocalDateTime createdDate,
		final LocalDateTime updateDate,
		final Set<BeerOrderLine> beerOrderLines,
		final Set<Category> categories
	) {
		this.id = id;
		this.version = version;
		this.beerName = beerName;
		this.beerStyle = beerStyle;
		this.upc = upc;
		this.quantityOnHand = quantityOnHand;
		this.price = price;
		this.createdDate = createdDate;
		this.updateDate = updateDate;
		this.beerOrderLines = beerOrderLines;
		this.setCategories(categories);
	}

	@Id
	@GeneratedValue
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;

	@Version
	private Integer version;

	@NotBlank
	@Size(max = 30)
	private String beerName;

	@NotNull
	private BeerStyle beerStyle;

	@NotBlank
	@Size(max = 255)
	private String upc;

	private Integer quantityOnHand;

	@NotNull
	@Positive
	private BigDecimal price;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@UpdateTimestamp
	private LocalDateTime updateDate;

	@OneToMany (mappedBy = "beer")
	@ToString.Exclude
	@Builder.Default
	private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

	@ManyToMany(mappedBy = "beers", fetch = FetchType.EAGER)
	@Builder.Default
	private Set<Category> categories = new HashSet<>();

	public void setCategories(final Set<Category> categories) {
		this.categories = categories;
		categories.forEach(category -> category.addBeer(this));
	}

	public void addCategory(Category category) {
		categories.add(category);
		category.getBeers().add(this);
	}

	public void removeCategory(Category category) {
		categories.remove(category);
		category.getBeers().remove(this);
	}

	@Override
	public final boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof final Beer beer)) {
			return false;
		}

		return Objects.equals(id, beer.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
