package br.com.sinapse.reports.sinapsereports.domain.report.gateway;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;

public interface ReportCommandGateway {
    ReportRequest create(ReportRequest reportRequest);

    ReportRequest findByID(UUID id);

    void update(ReportRequest reportRequest, ReportStatus status);
}
