package com.report.generation.service.report_generate_service.Application.Mapper;

import org.springframework.stereotype.Service;

import com.report.generation.service.report_generate_service.Application.Dtos.ReportRequestedEventDTO;
import com.report.generation.service.report_generate_service.Domain.Entities.Report;
import com.report.generation.service.report_generate_service.Application.Enum.ReportType;

@Service
public class ReportMapper {

    public Report toEntity(ReportRequestedEventDTO dto) {
        Report report = new Report();
        report.setId(dto.reportId());
        report.setReportType(ReportType.valueOf(dto.reportType()));
        report.setReportStartDate(dto.reportStartDate());
        report.setReportEndDate(dto.reportEndDate());
        report.setParameters(dto.parameters());
        report.setRequestedAt(dto.requestedAt());
        return report;
    }

    public ReportRequestedEventDTO toDTO(Report report) {
        return new ReportRequestedEventDTO(
                report.getId(),
                report.getReportType().name(),
                report.getReportStartDate(),
                report.getReportEndDate(),
                report.getParameters(),
                report.getRequestedAt());
    }
}
