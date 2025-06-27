package br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;
import br.com.sinapse.reports.sinapsereports.Infra.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Infra.Persistence.Repository.ReportRepositoryJpa;

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
    }

    @Override
    public ReportRequest findByID(UUID id) {

        return null;
    }

    @Override
    public void update(ReportRequest reportRequest, ReportStatus status) {
        reportRequest.updateStatus(status.name());

    }

}
