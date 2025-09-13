package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT obj FROM Sale obj WHERE :minDate <= obj.date and :maxDate >= obj.date and UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Sale> searchByMinMaxDateName(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    @Query("SELECT obj.seller.name as sellerName, SUM(obj.amount) as total FROM Sale obj WHERE :minDate <= obj.date and :maxDate >= obj.date GROUP BY obj.seller.name")
    Page<SaleSummaryProjection> searchSummary(LocalDate minDate, LocalDate maxDate, Pageable pageable);
}