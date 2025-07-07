package br.com.sinapse.reports.sinapsereports.application.report.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;
import jakarta.validation.constraints.NotBlank;

public record ReportRequestedEvent(
                @NotBlank UUID reportId,
                @NotBlank ReportType reportType,
                @NotBlank ReportStatus status,
                @NotBlank LocalDate reportStartDate,
                @NotBlank LocalDate reportEndDate,
                @NotBlank String parameters,
                @NotBlank LocalDateTime requestedAt) {
}
