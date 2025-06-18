package com.report.generation.service.report_generate_service.Domain.Exceptions.ValidationsRules;

import java.time.LocalDate;

import com.report.generation.service.report_generate_service.Domain.Entities.Report;
import com.report.generation.service.report_generate_service.Domain.Exceptions.Validators.AbstractValidator;

public class ReportValidationRules extends AbstractValidator<Report> {

    @Override
    protected void doValidate(Report target) {
        if (target.getReportType() == null) {
            addError("Report type cannot be null.");
        }

        // Validate 'reportStartDate'
        if (target.getReportStartDate() == null) {
            addError("Report start date cannot be null.");
        }

        // Validate 'reportEndDate'
        if (target.getReportEndDate() == null) {
            addError("Report end date cannot be null.");
        }

        // Validate date range if both dates are present
        if (target.getReportStartDate() != null && target.getReportEndDate() != null) {
            if (target.getReportStartDate().isAfter(target.getReportEndDate())) {
                addError("Report start date cannot be after report end date.");
            }
            if (target.getReportEndDate().isAfter(LocalDate.now())) {
                addError("Report end date cannot be in the future.");
            }
        }

        // Validate 'parameters' (assuming it's optional but if present, should not be
        // empty)
        // If 'parameters' is required, change this to:
        // if (target.getParameters() == null ||
        // target.getParameters().trim().isEmpty()) {
        // addError("Parameters cannot be null or empty if required.");
        // }
        // For now, let's assume it can be null or empty string if not used
        if (target.getParameters() != null && target.getParameters().trim().isEmpty()) {
            addError("Parameters cannot be an empty string if provided.");
        }

        // Note: 'status', 'requestedAt', 'linkDownload', and 'id' are typically
        // managed by the system/persistence layer and might not require initial
        // validation
        // from the user input perspective, or are set internally after initial
        // validation.
    }
}
