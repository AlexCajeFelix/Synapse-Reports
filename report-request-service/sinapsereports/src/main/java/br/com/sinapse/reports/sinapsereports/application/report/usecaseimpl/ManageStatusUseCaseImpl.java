package br.com.sinapse.reports.sinapsereports.application.report.usecaseimpl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.application.report.usecase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

@Service
public class ManageStatusUseCaseImpl extends ManageStatusUseCase {

    public ManageStatusUseCaseImpl(ReportCommandGateway reportCommandGateway) {
        super(reportCommandGateway);
    }

    @Override
    public void execute(ReportRequest report, ReportStatus status) {
        Objects.requireNonNull(status, "status não pode ser nulo");
        Objects.requireNonNull(report, "report não pode ser nulo");
        report.updateStatus(status.name());

        reportCommandGateway.update(report, status);

    }

}
