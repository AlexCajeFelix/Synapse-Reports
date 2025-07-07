package br.com.sinapse.reports.sinapsereports.application.report.usecaseimpl;

import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.application.report.usecase.GetReportUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

@Service
public class GetReportUseCaseImp extends GetReportUseCase {

    public GetReportUseCaseImp(ReportCommandGateway reportCommandGateway) {
        super(reportCommandGateway);

    }

    @Override
    public ReportRequest execute(UUID input) {
        Objects.requireNonNull(input);
        var reportRequest = reportCommandGateway.findByID(input);
        return reportRequest;
    }

}
