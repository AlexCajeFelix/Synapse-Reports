package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputNoOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

public abstract class PublishToKafkaUseCase extends InputNoOutputUseCase<ReportRequest> {

    @Override
    public abstract void execute(ReportRequest input);

}
