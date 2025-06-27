package br.com.sinapse.reports.sinapsereports.Infra.Mappers;

import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Infra.Persistence.EntitiesJpa.ReportRequestEntity;

public class ReportMapper {
    public static ReportRequestEntity toEntity(ReportRequest domain) {
        var entity = new ReportRequestEntity();

        entity.setId(domain.getId().getValue());
        entity.setStatus(domain.getStatus().name());
        entity.setReportType(domain.getReportType().name());
        entity.setReportStartDate(domain.getReportStartDate());
        entity.setReportEndDate(domain.getReportEndDate());
        entity.setParameters(domain.getParameters());
        entity.setRequestedAt(domain.getRequestedAt());

        return entity;
    }

    public static ReportRequest toDomain(ReportRequestEntity entity) {
        return ReportRequest.create(
                entity.getStatus(),
                entity.getReportType(),
                entity.getReportStartDate(),
                entity.getReportEndDate(),
                entity.getParameters());
    }
}
