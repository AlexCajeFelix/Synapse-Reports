package br.com.sinapse.reports.sinapsereports.infra.report.mappers;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequestID;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.entitiesjpa.ReportRequestEntity;

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
        return ReportRequest.with(
                ReportRequestID.from(entity.getId()),
                ReportStatus.from(entity.getStatus()).orElseThrow(),
                ReportType.from(entity.getReportType()).orElseThrow(),
                entity.getRequestedAt(),
                entity.getReportStartDate(),
                entity.getReportEndDate(),
                entity.getParameters());
    }

}
