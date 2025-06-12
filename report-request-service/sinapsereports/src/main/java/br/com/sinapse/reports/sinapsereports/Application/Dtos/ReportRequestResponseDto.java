package br.com.sinapse.reports.sinapsereports.Application.Dtos;

import java.util.UUID;

public record ReportRequestResponseDto(
                UUID reportId,
                String status,
                String message) {
}