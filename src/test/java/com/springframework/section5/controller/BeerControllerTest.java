package com.springframework.section5.controller;

import com.springframework.section5.model.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class BeerControllerTest {

	@Autowired
	BeerController controller;

	@Test
	void getBeerById() {
		Beer beer = controller.getBeerById(UUID.randomUUID());
		System.out.println(beer);
	}
}