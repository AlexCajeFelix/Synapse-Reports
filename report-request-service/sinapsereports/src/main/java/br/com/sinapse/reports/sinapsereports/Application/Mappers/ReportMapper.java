package br.com.sinapse.reports.sinapsereports.Application.Mappers; // Sugestão de pacote

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestedEvent;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportRequest toEntity(CreateReportRequestDto dto) {
        if (dto == null) {
            return null;
        }
        ReportRequest entity = new ReportRequest(
                dto.reportType(),
                dto.reportStartDate(),
                dto.reportEndDate(),
                dto.parameters());
        return entity;
    }

    public ReportRequestResponseDto toResponseDto(ReportRequest entity) {
        if (entity == null) {
            return null;
        }
        return new ReportRequestResponseDto(
                entity.getId(),
                entity.getStatus().toString(),
                "Seu pedido de relatório foi recebido e está na fila para processamento.");
    }

    public ReportRequestedEvent toEventDto(ReportRequest entity) {
        if (entity == null) {
            return null;
        }
        return new ReportRequestedEvent(entity.getId(), entity.getReportType(), entity.getReportStartDate(),
                entity.getReportEndDate(), entity.getParameters(), entity.getRequestedAt());

    }
}
