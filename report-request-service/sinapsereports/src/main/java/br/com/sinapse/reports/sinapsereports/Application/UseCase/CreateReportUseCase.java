package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import java.util.concurrent.CompletableFuture;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

public abstract class CreateReportUseCase
        extends InputOutputUseCase<CreateReportRequestDto, CompletableFuture<ReportRequest>> {

    protected final ReportCommandGateway reportCommandGateway;
    protected PublishToKafkaUseCase publishToKafkaUseCase;

    public CreateReportUseCase(ReportCommandGateway reportUseCase, PublishToKafkaUseCase publishToKafkaUseCase) {
        this.publishToKafkaUseCase = publishToKafkaUseCase;
        this.reportCommandGateway = reportUseCase;
    }

}
