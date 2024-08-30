package com.springframework.section5.service;

import com.springframework.section5.csvDataModel.BeerCsvRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface BeerCsvService {
	List<BeerCsvRecord> convertCsv(File file) throws FileNotFoundException;
}
