package br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository;

import java.util.List;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sinapse.reports.sinapsereports.infra.report.persistence.entitiesjpa.ReportRequestEntity;

public interface ReportRepositoryJpa extends JpaRepository<ReportRequestEntity, UUID> {

    @Query("SELECT r FROM ReportRequestEntity r WHERE r.status = :status")
    List<ReportRequestEntity> findByStatus(@Param("status") String status);

}
