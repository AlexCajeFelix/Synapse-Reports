package br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway;

import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

public interface PublishReportCommandGateway {
    void publishReport(ReportRequest reportRequest);
}
