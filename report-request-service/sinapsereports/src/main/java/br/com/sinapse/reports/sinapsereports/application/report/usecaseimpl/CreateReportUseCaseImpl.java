package br.com.sinapse.reports.sinapsereports.application.report.usecaseimpl;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.application.report.dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.application.report.dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.application.report.usecase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.application.report.usecase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.application.report.usecase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

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
    public ReportRequestResponseDto execute(CreateReportRequestDto aReportDto) {
        Objects.requireNonNull(aReportDto, "Objeto não pode ser nulo");
        log.info("Thread principal: " + Thread.currentThread().getName());

        var aReport = ReportRequest.create(
                STATUS_NAME,
                aReportDto.reportType(),
                aReportDto.reportStartDate(),
                aReportDto.reportEndDate(),
                aReportDto.parameters());
        log.info("Pedido criado, pendente para envio");

        var response = new ReportRequestResponseDto(aReport.getId().getValue().toString(),
                aReport.getReportType(),
                aReport.getStatus(),
                "Seu report esta sendo processado");

        CompletableFuture.runAsync(() -> {
            try {
                log.info("segunda thread: " + Thread.currentThread().getName());
                var savedReport = reportCommandGateway.create(aReport);
                publishToKafkaUseCase.execute(savedReport);
            } catch (Exception e) {
                log.error("Erro ao publicar no Kafka. Fallback será acionado automaticamente.", e);
            }
        }, IO_EXECUTOR).thenAcceptAsync(aVoid -> {
            manageStatusUseCase.execute(aReport, ReportStatus.PENDING);
            log.info("Status atualizado para PENDING_SEND");
        }, IO_EXECUTOR);

        return response;
    }

}
