package br.com.sinapse.reports.sinapsereports.Application.Dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
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
