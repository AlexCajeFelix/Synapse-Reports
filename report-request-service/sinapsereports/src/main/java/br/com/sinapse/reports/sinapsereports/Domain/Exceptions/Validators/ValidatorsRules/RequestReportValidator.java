package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorsRules;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.AbstractValidator;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorHandler;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.Error;

public class RequestReportValidator extends AbstractValidator {

    private final ReportRequest reportRequest;

    public RequestReportValidator(ValidatorHandler handler, ReportRequest reportRequest) {
        super(handler);
        this.reportRequest = reportRequest;
    }

    @Override
    public void validate() {
        if (this.reportRequest.getId().getValue() == null) {
            this.handler.append(new Error("id nao pode ser nulo"));
        }

        if (this.reportRequest.getStatus() == null) {
            this.handler.append(new Error("status nao pode ser nulo"));
        }

    }

}
