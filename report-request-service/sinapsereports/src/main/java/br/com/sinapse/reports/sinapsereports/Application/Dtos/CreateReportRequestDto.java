package br.com.sinapse.reports.sinapsereports.Application.Dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequestDto(
        @NotBlank String status,
        @NotBlank String reportType,
        @NotNull LocalDate reportStartDate,
        @NotNull LocalDate reportEndDate,
        @NotBlank String parameters) {
}
