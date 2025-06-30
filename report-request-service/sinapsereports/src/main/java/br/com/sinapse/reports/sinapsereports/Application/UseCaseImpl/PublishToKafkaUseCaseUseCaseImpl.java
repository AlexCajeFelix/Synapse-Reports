package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.PublishReportCommandGateway;

@Service
public class PublishToKafkaUseCaseUseCaseImpl extends PublishToKafkaUseCase {

    public PublishToKafkaUseCaseUseCaseImpl(PublishReportCommandGateway publishReportCommandGateway,
            ManageStatusUseCase mangageStatusUseCase) {
        super(publishReportCommandGateway, mangageStatusUseCase);

    }

    @Override
    public void execute(ReportRequest report) {
        Objects.requireNonNull(report, "Objeto nao pode ser nulo");
        publishReportCommandGateway.publishReport(report);

    }

}
