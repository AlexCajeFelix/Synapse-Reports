package br.com.sinapse.reports.sinapsereports.application.report.dtos;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;

public record ReportRequestResponseDto(
                UUID reportId,
                ReportType reportType,
                ReportStatus status,
                String message) {
}