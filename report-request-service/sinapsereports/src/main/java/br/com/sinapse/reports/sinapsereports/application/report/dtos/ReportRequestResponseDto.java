package br.com.sinapse.reports.sinapsereports.application.report.dtos;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;

public record ReportRequestResponseDto(
        String reportId,
        ReportType reportType,
        ReportStatus status,
        String message) {
}
