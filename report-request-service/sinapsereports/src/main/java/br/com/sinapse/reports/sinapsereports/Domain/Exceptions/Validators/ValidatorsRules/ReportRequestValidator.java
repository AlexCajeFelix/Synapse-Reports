package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorsRules;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.AbstractValidator;

@Component
public class ReportRequestValidator extends AbstractValidator<ReportRequest> {

    @Override
    protected void doValidate(ReportRequest target) {
        if (target == null) {
            addError("ReportRequest must not be null.");
            return;
        }

        if (target.getReportType() == null || isBlank(target.getReportType().name())) {
            addError("Report type must not be blank.");
        }

        /*
         * if (target.getStatus() == null || isBlank(target.getStatus().name())) {
         * addError("Status must not be blank.");
         * }
         */

        if (target.getReportStartDate() == null || target.getReportEndDate() == null) {
            addError("Start and end dates must not be null.");
        } else {
            if (target.getReportStartDate().isAfter(target.getReportEndDate())) {
                addError("Start date must be before or equal to end date.");
            }

            if (target.getReportStartDate().isAfter(LocalDate.now())) {
                addError("Start date must not be in the future.");
            }

            if (target.getReportEndDate().isAfter(LocalDate.now())) {
                addError("End date must not be in the future.");
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
