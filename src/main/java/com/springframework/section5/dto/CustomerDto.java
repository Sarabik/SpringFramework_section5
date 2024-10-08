package com.springframework.section5.dto;

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
public class CustomerDto {
	private UUID id;
	private String customerName;
	private String email;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
}
