package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputTwoParamsNoOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

public abstract class ManageStatusUseCase extends InputTwoParamsNoOutputUseCase<ReportRequest, ReportStatus> {

    protected final ReportCommandGateway reportCommandGateway;

    public ManageStatusUseCase(ReportCommandGateway reportCommandGateway) {
        this.reportCommandGateway = reportCommandGateway;
    }

}
