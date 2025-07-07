package br.com.sinapse.reports.sinapsereports.infra.report.gatewayimpl;

import java.util.UUID;
import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;
import br.com.sinapse.reports.sinapsereports.infra.report.mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository.ReportRepositoryJpa;

@Service
public class CreateReportGatewayImpl implements ReportCommandGateway {

    private final ReportRepositoryJpa reportRepositoryJpa;

    public CreateReportGatewayImpl(ReportRepositoryJpa reportRepositoryJpa) {
        this.reportRepositoryJpa = reportRepositoryJpa;
    }

    @Override
    public ReportRequest create(ReportRequest reportRequest) {
        var entityJpa = ReportMapper.toEntity(reportRequest);
        var result = reportRepositoryJpa.save(entityJpa);
        if (result == null) {
            throw new RuntimeException("Erro ao salvar report");
        }

        return reportRequest;

        // throw new RuntimeException("Erro ao salvar report");
    }

    @Override
    public ReportRequest findByID(UUID id) {
        return reportRepositoryJpa.findById(id).map(ReportMapper::toDomain).orElse(null);
    }

    @Override
    public void update(ReportRequest reportRequest, ReportStatus status) {
        var entityJpa = ReportMapper.toEntity(reportRequest);
        var result = reportRepositoryJpa.save(entityJpa);
        if (result == null) {
            throw new RuntimeException("Erro ao salvar report");
        }

    }

}
