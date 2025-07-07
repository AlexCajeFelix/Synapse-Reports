package br.com.sinapse.reports.sinapsereports.application.report.usecase;

import br.com.sinapse.reports.sinapsereports.application.shared.UseCaseAbstract.InputNoOutputUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.PublishReportCommandGateway;

public abstract class PublishToKafkaUseCase extends InputNoOutputUseCase<ReportRequest> {

    protected final PublishReportCommandGateway publishReportCommandGateway;

    protected final ManageStatusUseCase manageStatusUseCase;

    public PublishToKafkaUseCase(PublishReportCommandGateway publishReportCommandGateway,
            ManageStatusUseCase manageStatusUseCase) {
        this.publishReportCommandGateway = publishReportCommandGateway;
        this.manageStatusUseCase = manageStatusUseCase;
    }

}
