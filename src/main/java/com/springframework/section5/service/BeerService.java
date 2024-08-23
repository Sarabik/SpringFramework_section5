package com.springframework.section5.service;

import com.springframework.section5.model.Beer;

import java.util.UUID;

public interface BeerService {

	Beer getBeerById(UUID id);

}
