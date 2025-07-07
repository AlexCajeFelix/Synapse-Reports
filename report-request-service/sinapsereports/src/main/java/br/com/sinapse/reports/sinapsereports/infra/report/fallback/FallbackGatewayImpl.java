package br.com.sinapse.reports.sinapsereports.infra.report.fallback;

import org.springframework.stereotype.Component;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.infra.report.mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository.ReportRepositoryJpa;

@Component
public class FallbackGatewayImpl {

    private final ReportRepositoryJpa reportRepositoryJpa;

    public FallbackGatewayImpl(ReportRepositoryJpa reportRepositoryJpa) {
        this.reportRepositoryJpa = reportRepositoryJpa;
    }

    final ReportStatus FAILED = ReportStatus.FAILED;

    public void publishReport(ReportRequest reportRequest) {

        reportRequest.updateStatus(FAILED.name());

        reportRepositoryJpa.save(ReportMapper.toEntity(reportRequest));

    }

}
