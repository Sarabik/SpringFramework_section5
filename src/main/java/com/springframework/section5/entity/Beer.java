package com.springframework.section5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Beer {
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
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
	private LocalDateTime createdDate;
	private LocalDateTime updateDate;

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
