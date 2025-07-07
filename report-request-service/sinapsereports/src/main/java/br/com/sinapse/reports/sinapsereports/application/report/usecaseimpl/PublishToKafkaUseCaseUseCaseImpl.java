package br.com.sinapse.reports.sinapsereports.application.report.usecaseimpl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.application.report.usecase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.application.report.usecase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.PublishReportCommandGateway;

@Service
public class PublishToKafkaUseCaseUseCaseImpl extends PublishToKafkaUseCase {

    public PublishToKafkaUseCaseUseCaseImpl(PublishReportCommandGateway publishReportCommandGateway,
            ManageStatusUseCase manageStatusUseCase) {
        super(publishReportCommandGateway, manageStatusUseCase);

    }

    @Override
    public void execute(ReportRequest report) {
        Objects.requireNonNull(report, "Objeto nao pode ser nulo");
        publishReportCommandGateway.publishReport(report);

    }

}
