package br.com.sinapse.reports.sinapsereports.Application.Dtos;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.NotBlank;

public record CreateReportRequestDto(
                @NotBlank String reportType,
                @NonNull LocalDate reportStartDate,
                @NonNull LocalDate reportEndDate,
                @NotBlank String parameters) {
}