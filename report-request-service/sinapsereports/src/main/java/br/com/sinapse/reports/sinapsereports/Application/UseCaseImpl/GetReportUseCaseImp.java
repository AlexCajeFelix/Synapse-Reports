package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.GetReportUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

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
