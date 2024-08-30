package com.springframework.section5.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.springframework.section5.csvDataModel.BeerCsvRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
	@Override
	public List<BeerCsvRecord> convertCsv(final File file) {
		try {
			List<BeerCsvRecord> beerCsvRecords = new CsvToBeanBuilder<BeerCsvRecord>(
				new FileReader(file)).withType(BeerCsvRecord.class).build().parse();
			return beerCsvRecords;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
