package br.com.sinapse.reports.sinapsereports.Application.Dtos;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;

public record ReportRequestResponseDto(
                UUID reportId,
                ReportType reportType,
                ReportStatus status,
                String message) {
}