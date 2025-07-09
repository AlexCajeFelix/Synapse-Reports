package br.com.sinapse.reports.sinapsereports.domain.report.Validator;

import java.time.LocalDate;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.AbstractValidator;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Error;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Notification;

public class RequestReportValidator extends AbstractValidator<ReportRequest> {

    public RequestReportValidator(ReportRequest anEntity, Notification aNotification) {
        super(anEntity, aNotification);
    }

    @Override
    public void validate() {
        this.validateReportRequest();
    }

    private void validateReportRequest() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = this.entity.getReportStartDate();
        LocalDate endDate = this.entity.getReportEndDate();

        if (this.entity.getId().getValue() == null) {
            this.notification.append(new Error("O ID não pode ser nulo"));
        }

        if (startDate == null) {
            this.notification.append(new Error("A data de início não pode ser nula"));
        } else {
            if (startDate.isAfter(today)) {
                this.notification.append(new Error("A data de início não pode ser no futuro"));
            }
            if (endDate != null && startDate.isAfter(endDate)) {
                this.notification.append(new Error("A data de início não pode ser depois da data de fim"));
            }
        }

        if (endDate == null) {
            this.notification.append(new Error("A data de fim não pode ser nula"));
        } else {
            if (endDate.isAfter(today)) {
                this.notification.append(new Error("A data de fim não pode ser no futuro"));
            }
        }
    }

}