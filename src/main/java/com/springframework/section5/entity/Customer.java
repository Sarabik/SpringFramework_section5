package com.springframework.section5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Customer {
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
	private UUID id;

	@Version
	private Integer version;

	private String customerName;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;

	@Override
	public final boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof final Customer customer)) {
			return false;
		}

		return Objects.equals(id, customer.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
