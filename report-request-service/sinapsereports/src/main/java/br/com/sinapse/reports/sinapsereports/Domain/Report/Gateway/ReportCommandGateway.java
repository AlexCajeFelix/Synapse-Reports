package br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;

public interface ReportCommandGateway {
    ReportRequest create(ReportRequest reportRequest);

    ReportRequest findByID(UUID id);

    void update(ReportRequest reportRequest, ReportStatus status);
}
