package com.springframework.section5.service;

import com.springframework.section5.csvDataModel.BeerCsvRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeerCsvServiceImplTest {

	BeerCsvService beerCsvService = new BeerCsvServiceImpl();

	@Test
	void testConvertCSV() throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
		List<BeerCsvRecord> recs = beerCsvService.convertCsv(file);
		System.out.println(recs.size());
		assertThat(recs.size()).isGreaterThan(0);
	}

}