package br.com.sinapse.reports.sinapsereports.domain.report.exceptions.validators.validatorrules;

import java.time.LocalDate;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.AbstractValidator;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Error;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.ValidatorHandler;

public class RequestReportValidator extends AbstractValidator {

    private final ReportRequest reportRequest;

    public RequestReportValidator(ValidatorHandler handler, ReportRequest reportRequest) {
        super(handler);
        this.reportRequest = reportRequest;
    }

    @Override
    public void validate() {
        this.validateReportRequest();
    }

    private void validateReportRequest() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = this.reportRequest.getReportStartDate();
        LocalDate endDate = this.reportRequest.getReportEndDate();

        if (this.reportRequest.getId().getValue() == null) {
            this.handler.append(new Error("O ID não pode ser nulo"));
        }

        if (startDate == null) {
            this.handler.append(new Error("A data de início não pode ser nula"));
        } else {
            if (startDate.isAfter(today)) {
                this.handler.append(new Error("A data de início não pode ser no futuro"));
            }
            if (endDate != null && startDate.isAfter(endDate)) {
                this.handler.append(new Error("A data de início não pode ser depois da data de fim"));
            }
        }

        if (endDate == null) {
            this.handler.append(new Error("A data de fim não pode ser nula"));
        } else {
            if (endDate.isAfter(today)) {
                this.handler.append(new Error("A data de fim não pode ser no futuro"));
            }
        }
    }

}
