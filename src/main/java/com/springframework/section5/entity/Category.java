package com.springframework.section5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Category {

	public Category(
		final UUID id,
		final String description,
		final Instant createdDate,
		final Instant lastModifiedDate,
		final Long version,
		final Set<Beer> beers
	) {
		this.id = id;
		this.description = description;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.version = version;
		this.setBeers(beers);
	}

	@Id
	@GeneratedValue
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;

	@Size(max = 50)
	private String description;

	@CreationTimestamp
	private Instant createdDate;

	@UpdateTimestamp
	private Instant lastModifiedDate;

	@Version
	private Long version;

	@ManyToMany
	@JoinTable(name = "beer_category",
		joinColumns = @JoinColumn(name = "category_id"),
		inverseJoinColumns = @JoinColumn(name = "beer_id"))
	@ToString.Exclude
	@Builder.Default
	private Set<Beer> beers = new HashSet<>();

	public void setBeers(final Set<Beer> beers) {
		this.beers = beers;
		beers.forEach(beer -> beer.addCategory(this));
	}

	public void addBeer(Beer beer) {
		beers.add(beer);
		beer.getCategories().add(this);
	}

	public void removeBeer(Beer beer) {
		beers.remove(beer);
		beer.getCategories().remove(this);
	}
}