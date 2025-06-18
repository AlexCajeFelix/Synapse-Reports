package com.report.generation.service.report_generate_service.Application.UseCase;

import com.report.generation.service.report_generate_service.Domain.Entities.Report;

public abstract class ProducerReportUseCase {

    public abstract void sendReportToKafka(Report report);

    public abstract void sendReportToRabbitMQ(Report report);
}
