package br.com.sinapse.reports.sinapsereports.application.report.usecase;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.application.shared.UseCaseAbstract.InputOutputUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

public abstract class GetReportUseCase extends InputOutputUseCase<UUID, ReportRequest> {

    protected final ReportCommandGateway reportCommandGateway;

    public GetReportUseCase(ReportCommandGateway reportCommandGateway) {
        this.reportCommandGateway = reportCommandGateway;
    }

}
