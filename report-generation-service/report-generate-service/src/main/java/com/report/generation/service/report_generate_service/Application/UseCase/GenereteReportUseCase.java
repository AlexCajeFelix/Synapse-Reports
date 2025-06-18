package com.report.generation.service.report_generate_service.Application.UseCase;

import com.report.generation.service.report_generate_service.Domain.Entities.Report;

public abstract class GenereteReportUseCase {

    public abstract void generateReport(Report report);
}
