package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CreateReportUseCaseImpl extends CreateReportUseCase {

    private final String STATUS_NAME = ReportStatus.PENDING_SEND.name();
    private final Logger log = LoggerFactory.getLogger(getClass());

    public CreateReportUseCaseImpl(ReportCommandGateway reportCommandGateway,
            PublishToKafkaUseCase publishToKafkaUseCase, ManageStatusUseCase manageStatusUseCase) {
        super(reportCommandGateway, publishToKafkaUseCase, manageStatusUseCase);

    }

    @Override
    public ReportRequest execute(CreateReportRequestDto aReportDto) {
        Objects.requireNonNull(aReportDto, "Objeto não pode ser nulo");

        log.info("Thread principal: " + Thread.currentThread().getName());

        var aReport = ReportRequest.create(
                STATUS_NAME,
                aReportDto.reportType(),
                aReportDto.reportStartDate(),
                aReportDto.reportEndDate(),
                aReportDto.parameters());
        log.info("Pedido criado, pendente para envio");

        CompletableFuture.runAsync(() -> {
            try {
                var savedReport = reportCommandGateway.create(aReport);
                publishToKafkaUseCase.execute(savedReport);
            } catch (Exception e) {
                log.error("Erro ao publicar no Kafka. Fallback será acionado automaticamente.", e);

            }
        });

        return aReport;
    }

}
