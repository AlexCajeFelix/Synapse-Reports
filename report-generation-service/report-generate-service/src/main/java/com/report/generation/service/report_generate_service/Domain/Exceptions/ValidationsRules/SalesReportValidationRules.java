package com.report.generation.service.report_generate_service.Domain.Exceptions.ValidationsRules;

import java.time.LocalDate;

import com.report.generation.service.report_generate_service.Domain.Entities.SalesReportEntity;
import com.report.generation.service.report_generate_service.Domain.Exceptions.Validators.AbstractValidator;

public class SalesReportValidationRules extends AbstractValidator<SalesReportEntity> {

    @Override
    protected void doValidate(SalesReportEntity target) {

        if (target.getProduct() == null || target.getProduct().trim().isEmpty()) {
            addError("Product cannot be null or empty.");
        }

        if (target.getSaleDate() == null) {
            addError("Sale date cannot be null.");
        } else if (target.getSaleDate().isAfter(LocalDate.now())) {
            addError("Sale date cannot be in the future.");
        }

        if (target.getDetails() == null || target.getDetails().trim().isEmpty()) {
            addError("Details cannot be null or empty.");
        }

    }
}
