package com.report.generation.service.report_generate_service.Application.UseCase;

import java.util.function.Consumer;

import com.report.generation.service.report_generate_service.Application.Dtos.ReportRequestedEventDTO;

public abstract class ReportRequestedConsumerUseCase {

    public abstract Consumer<ReportRequestedEventDTO> consumeReportRequestedEvent();

}
