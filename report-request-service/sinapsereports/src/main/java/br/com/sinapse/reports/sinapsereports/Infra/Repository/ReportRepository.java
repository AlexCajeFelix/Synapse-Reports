package br.com.sinapse.reports.sinapsereports.Infra.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

public interface ReportRepository extends JpaRepository<ReportRequest, UUID> {

}
