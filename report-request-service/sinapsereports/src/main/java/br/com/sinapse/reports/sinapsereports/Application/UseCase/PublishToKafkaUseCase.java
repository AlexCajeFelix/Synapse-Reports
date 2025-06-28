package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputNoOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.PublishReportCommandGateway;

public abstract class PublishToKafkaUseCase extends InputNoOutputUseCase<ReportRequest> {

    protected final PublishReportCommandGateway publishReportCommandGateway;

    protected final ManageStatusUseCase manageStatusUseCase;

    public PublishToKafkaUseCase(PublishReportCommandGateway publishReportCommandGateway,
            ManageStatusUseCase manageStatusUseCase) {
        this.publishReportCommandGateway = publishReportCommandGateway;
        this.manageStatusUseCase = manageStatusUseCase;
    }

}
