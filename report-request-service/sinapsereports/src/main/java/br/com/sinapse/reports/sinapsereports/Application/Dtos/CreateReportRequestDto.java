package br.com.sinapse.reports.sinapsereports.Application.Dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CreateReportRequestDto(
        @NotBlank String reportType,
        @NotBlank LocalDate reportStartDate,
        @NotBlank LocalDate reportEndDate,
        @NotBlank String parameters) {
}