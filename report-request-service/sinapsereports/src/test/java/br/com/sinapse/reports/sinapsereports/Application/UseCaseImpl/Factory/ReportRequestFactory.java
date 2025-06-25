package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Factory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

public class ReportRequestFactory {

    private static ReportRequest genereteEntity(UUID id, ReportStatus status, ReportType reportType,
            LocalDate reportStartDate,
            LocalDate reportEndDate, String parameters) {
        return new ReportRequest(
                id,
                status,
                reportType,
                LocalDateTime.now(),
                reportStartDate,
                reportEndDate,
                parameters);
    }

    public static ReportRequest createValidReportRequest() {
        var report = genereteEntity(
                UUID.randomUUID(),
                ReportStatus.PENDING,
                ReportType.PDF,
                LocalDate.now(),
                LocalDate.now(),
                "parameters");
        return report;

    }
}
