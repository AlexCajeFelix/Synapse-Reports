package br.com.sinapse.reports.sinapsereports.application.report.usecase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;
import br.com.sinapse.reports.sinapsereports.application.report.dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.application.report.dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.application.shared.UseCaseAbstract.InputOutputUseCase;

public abstract class CreateReportUseCase
        extends InputOutputUseCase<CreateReportRequestDto, ReportRequestResponseDto> {

    protected final ReportCommandGateway reportCommandGateway;
    protected final PublishToKafkaUseCase publishToKafkaUseCase;
    protected final ManageStatusUseCase manageStatusUseCase;

    protected static final Executor IO_EXECUTOR = Executors.newFixedThreadPool(4);

    public CreateReportUseCase(ReportCommandGateway reportUseCase, PublishToKafkaUseCase publishToKafkaUseCase,
            ManageStatusUseCase manageStatusUseCase) {
        this.manageStatusUseCase = manageStatusUseCase;
        this.publishToKafkaUseCase = publishToKafkaUseCase;
        this.reportCommandGateway = reportUseCase;

    }

}
