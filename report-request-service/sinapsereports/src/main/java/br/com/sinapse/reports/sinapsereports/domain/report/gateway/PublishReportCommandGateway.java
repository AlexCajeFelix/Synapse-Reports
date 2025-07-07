package br.com.sinapse.reports.sinapsereports.domain.report.gateway;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;

public interface PublishReportCommandGateway {
    void publishReport(ReportRequest reportRequest);
}
