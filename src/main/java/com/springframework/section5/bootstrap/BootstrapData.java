package com.springframework.section5.bootstrap;

import com.springframework.section5.entity.Beer;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.entity.Customer;
import com.springframework.section5.repository.BeerRepository;
import com.springframework.section5.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

	private final BeerRepository beerRepository;
	private final CustomerRepository customerRepository;

	public BootstrapData(final BeerRepository beerRepository, final CustomerRepository customerRepository) {
		this.beerRepository = beerRepository;
		this.customerRepository = customerRepository;
	}

	@Override
	public void run(final String... args) throws Exception {
		if (beerRepository.count() == 0) {
			addBeerData();
		}
		if (customerRepository.count() == 0) {
			addCustomerData();
		}
	}

	private void addBeerData() {
		Beer beer1 = Beer.builder()
			.beerName("Galaxy Cat")
			.beerStyle(BeerStyle.PALE_ALE)
			.upc("12356")
			.price(new BigDecimal("12.99"))
			.quantityOnHand(122)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		Beer beer2 = Beer.builder()
			.beerName("Crank")
			.beerStyle(BeerStyle.PALE_ALE)
			.upc("12356222")
			.price(new BigDecimal("11.99"))
			.quantityOnHand(392)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		Beer beer3 = Beer.builder()
			.beerName("Sunshine City")
			.beerStyle(BeerStyle.IPA)
			.upc("12356")
			.price(new BigDecimal("13.99"))
			.quantityOnHand(144)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		beerRepository.saveAll(List.of(beer1, beer2, beer3));
	}

	private void addCustomerData() {
		Customer customer1 = Customer.builder()
			.customerName("name1")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		Customer customer2 = Customer.builder()
			.customerName("name2")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		Customer customer3 = Customer.builder()
			.customerName("name3")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		customerRepository.saveAll(List.of(customer1, customer2, customer3));
	}
}
