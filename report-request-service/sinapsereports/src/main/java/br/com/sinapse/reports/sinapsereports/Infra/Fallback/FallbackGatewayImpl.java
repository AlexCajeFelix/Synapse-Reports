package br.com.sinapse.reports.sinapsereports.Infra.Fallback;

import org.springframework.stereotype.Component;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;

import br.com.sinapse.reports.sinapsereports.Infra.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Infra.Persistence.Repository.ReportRepositoryJpa;

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
