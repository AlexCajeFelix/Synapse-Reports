package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Application.UseCase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.PublishReportCommandGateway;

@Service
public class PublishToKafkaUseCaseUseCaseImpl extends PublishToKafkaUseCase {

    public PublishToKafkaUseCaseUseCaseImpl(PublishReportCommandGateway publishReportCommandGateway) {
        super(publishReportCommandGateway);

    }

    @Override
    public void execute(ReportRequest report) {
        publishReportCommandGateway.publishReport(report);
    }

}
