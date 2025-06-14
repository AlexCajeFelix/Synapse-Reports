package br.com.sinapse.reports.sinapsereports.Infra.Repository;

import java.util.List;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

public interface ReportRepository extends JpaRepository<ReportRequest, UUID> {

    @Query("SELECT r FROM ReportRequest r WHERE r.status = :pendenteEnvio")
    List<ReportRequest> findByStatus(@Param("pendenteEnvio") ReportStatus pendenteEnvio);

}
