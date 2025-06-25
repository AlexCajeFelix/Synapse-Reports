package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorsRules;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.AbstractValidator;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorHandler;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

public class RequestReportValidator extends AbstractValidator {

    private final ReportRequest reportRequest;

    public RequestReportValidator(ValidatorHandler handler, ReportRequest reportRequest) {
        super(handler);
        this.reportRequest = reportRequest;
    }

    @Override
    public void validate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

}
