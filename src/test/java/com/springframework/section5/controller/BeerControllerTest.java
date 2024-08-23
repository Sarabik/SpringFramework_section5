package com.springframework.section5.controller;

import com.springframework.section5.model.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerControllerTest {

	@Autowired
	BeerController controller;

	@Test
	void listBeers() {
		System.out.println(controller.listBeers());
	}

	@Test
	void getBeerById() {
		Beer beer = controller.getBeerById(controller.listBeers().get(0).getId());
		System.out.println(beer);
	}
}