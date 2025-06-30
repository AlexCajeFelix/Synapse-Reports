package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import java.util.concurrent.CompletableFuture;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

public abstract class CreateReportUseCase
        extends InputOutputUseCase<CreateReportRequestDto, ReportRequest> {

    protected final ReportCommandGateway reportCommandGateway;
    protected final PublishToKafkaUseCase publishToKafkaUseCase;
    protected final ManageStatusUseCase manageStatusUseCase;

    public CreateReportUseCase(ReportCommandGateway reportUseCase, PublishToKafkaUseCase publishToKafkaUseCase,
            ManageStatusUseCase manageStatusUseCase) {
        this.manageStatusUseCase = manageStatusUseCase;
        this.publishToKafkaUseCase = publishToKafkaUseCase;
        this.reportCommandGateway = reportUseCase;
    }

}
