package com.springframework.section5.dto;

import com.springframework.section5.entity.BeerStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
	private UUID id;
	@NotBlank
	private String beerName;
	@NotNull
	private BeerStyle beerStyle;
	@NotBlank
	private String upc;
	private Integer quantityOnHand;
	@NotNull
	@Positive
	private BigDecimal price;
	private LocalDateTime createdDate;
	private LocalDateTime updateDate;
}
