package br.com.sinapse.reports.sinapsereports.Infra.Persistence.Repository;

import java.util.List;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

import br.com.sinapse.reports.sinapsereports.Infra.Persistence.EntitiesJpa.ReportRequestEntity;

public interface ReportRepositoryJpa extends JpaRepository<ReportRequestEntity, UUID> {

    @Query("SELECT r FROM ReportRequestEntity r WHERE r.status = :pendenteEnvio")
    List<ReportRequest> findByStatus(@Param("pendenteEnvio") ReportRequestEntity pendenteEnvio);

}
