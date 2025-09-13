package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleDTO> getReport(String minDateStr, String maxDateStr, String name, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate minDate;
		LocalDate maxDate;

		if (maxDateStr == null) {
			maxDate = today;
		} else {
			maxDate = LocalDate.parse(maxDateStr);
		}

		if (minDateStr == null) {
			minDate = maxDate.minusYears(1L);
		} else {
			minDate = LocalDate.parse(minDateStr);
		}


		Page<Sale> result = repository.searchByMinMaxDateName(minDate, maxDate, name, pageable);
		return result.map(x -> new SaleDTO(x));
	}

	public Page<SaleSummaryDTO> getSummary(String minDateStr, String maxDateStr, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate minDate;
		LocalDate maxDate;

		if (maxDateStr == null) {
			maxDate = today;
		} else {
			maxDate = LocalDate.parse(maxDateStr);
		}

		if (minDateStr == null) {
			minDate = maxDate.minusYears(1L);
		} else {
			minDate = LocalDate.parse(minDateStr);
		}

		Page<SaleSummaryProjection> result = repository.searchSummary(minDate, maxDate, pageable);
		return result.map(x -> new SaleSummaryDTO(x.getSellerName(), x.getTotal()));
	}
}
