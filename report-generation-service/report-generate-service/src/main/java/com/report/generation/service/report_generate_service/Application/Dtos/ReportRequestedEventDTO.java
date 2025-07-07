package com.report.generation.service.report_generate_service.Application.Dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReportRequestedEventDTO(UUID reportId, String reportType, LocalDate reportStartDate,
        LocalDate reportEndDate,
        String parameters, LocalDateTime requestedAt) {

}
