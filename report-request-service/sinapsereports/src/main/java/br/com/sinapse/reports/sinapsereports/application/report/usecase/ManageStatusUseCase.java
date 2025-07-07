package br.com.sinapse.reports.sinapsereports.application.report.usecase;

import br.com.sinapse.reports.sinapsereports.application.shared.UseCaseAbstract.InputTwoParamsNoOutputUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

public abstract class ManageStatusUseCase extends InputTwoParamsNoOutputUseCase<ReportRequest, ReportStatus> {

    protected final ReportCommandGateway reportCommandGateway;

    public ManageStatusUseCase(ReportCommandGateway reportCommandGateway) {
        this.reportCommandGateway = reportCommandGateway;
    }

}
