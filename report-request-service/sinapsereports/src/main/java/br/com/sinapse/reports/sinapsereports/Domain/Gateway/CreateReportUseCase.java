package br.com.sinapse.reports.sinapsereports.Domain.Gateway;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

public interface CreateReportUseCase {
    ReportRequest create(ReportRequest reportRequest);

    ReportRequest findByID(UUID id);
}
