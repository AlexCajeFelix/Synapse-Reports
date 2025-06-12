package br.com.sinapse.reports.sinapsereports.Domain.Validators;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

@Component
public class ReportRequestValidator {

    public static void validate(ReportRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("ReportRequest must not be null.");
        }

        if (isBlank(request.getReportType())) {
            throw new IllegalArgumentException("Report type must not be blank.");
        }

        if (request.getReportStartDate() == null || request.getReportEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates must not be null.");
        }

        // startDate deve ser antes ou igual a endDate
        if (request.getReportStartDate().isAfter(request.getReportEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        // Nenhuma data pode ser depois de hoje
        if (request.getReportStartDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Start date must not be in the future.");
        }

        if (request.getReportEndDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("End date must not be in the future.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
