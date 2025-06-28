package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

public abstract class GetReportUseCase extends InputOutputUseCase<UUID, ReportRequest> {

    protected final ReportCommandGateway reportCommandGateway;

    public GetReportUseCase(ReportCommandGateway reportCommandGateway) {
        this.reportCommandGateway = reportCommandGateway;
    }

}
