package com.report.generation.service.report_generate_service.Application.UseCaseImpl;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Service;

import com.report.generation.service.report_generate_service.Application.Dtos.ReportRequestedEventDTO;

import com.report.generation.service.report_generate_service.Application.Mapper.ReportMapper;
import com.report.generation.service.report_generate_service.Application.UseCase.GenereteReportUseCase;
import com.report.generation.service.report_generate_service.Application.UseCase.ReportRequestedConsumerUseCase;
import com.report.generation.service.report_generate_service.Domain.Entities.Report;

@Service
public class ReportRequestedConsumerUseCaseImpl extends ReportRequestedConsumerUseCase {

    private final ReportMapper reportMapper;

    private final GenereteReportUseCase genereteReposrtUSeCase;

    public ReportRequestedConsumerUseCaseImpl(ReportMapper reportMapper,
            GenereteReportUseCase genereteReposrtUSeCase) {
        this.reportMapper = reportMapper;
        this.genereteReposrtUSeCase = genereteReposrtUSeCase;
    }

    @Override
    @Bean
    public Consumer<ReportRequestedEventDTO> consumeReportRequestedEvent() {
        return event -> {

            Report report = reportMapper.toEntity(event);
            genereteReposrtUSeCase.generateReport(report);

        };
    }

}
