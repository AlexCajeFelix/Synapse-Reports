package com.report.generation.service.report_generate_service.Infra.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.report.generation.service.report_generate_service.Domain.Entities.SalesReportEntity;

public interface ReportRepository extends JpaRepository<SalesReportEntity, UUID> {

    @Query("SELECT r FROM SalesReportEntity r WHERE r.saleDate BETWEEN :startDate AND :endDate")
    List<SalesReportEntity> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
