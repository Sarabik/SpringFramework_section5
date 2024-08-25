package com.springframework.section5.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	private UUID id;
	private String customerName;
	private Integer version;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
}
