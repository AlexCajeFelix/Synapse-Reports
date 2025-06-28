package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

@Service
public class ManageStatusUseCaseImpl extends ManageStatusUseCase {

    public ManageStatusUseCaseImpl(ReportCommandGateway reportCommandGateway) {
        super(reportCommandGateway);
    }

    @Override
    public void execute(ReportRequest report, ReportStatus status) {

        report.updateStatus(status.name());

        reportCommandGateway.update(report, status);

    }

}
