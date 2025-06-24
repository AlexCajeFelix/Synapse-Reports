package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;

@Service
public class CreateReportUseCaseImpl extends CreateReportUseCase {

    private final PublishToKafkaUseCase publishToKafkaUseCaseUseCase;
    private static final Logger log = LoggerFactory.getLogger(CreateReportUseCaseImpl.class);
    private final ReportMapper reportMapper;
    private final ManageStatusUseCaseImpl manageStatusUseCase;

    public CreateReportUseCaseImpl(PublishToKafkaUseCase publishToKafkaUseCaseUseCase, ReportMapper reportMapper,
            ManageStatusUseCaseImpl manageStatusUseCase, ManageStatusUseCaseImpl manageStatusUseCase1) {
        this.publishToKafkaUseCaseUseCase = publishToKafkaUseCaseUseCase;
        this.reportMapper = reportMapper;
        this.manageStatusUseCase = manageStatusUseCase;

    }

    @Override
    @Async("reportTaskExecutor")
    public CompletableFuture<ReportRequestResponseDto> execute(ReportRequest reportRequest) {
        manageStatusUseCase.execute(reportRequest, ReportStatus.PENDING_SEND);
        log.info("Solicitação {} criada com sucesso.", reportRequest.getId());

        return CompletableFuture.supplyAsync(() -> {
            publishToKafkaUseCaseUseCase.execute(reportRequest);
            return reportMapper.toResponseDto(reportRequest);
        }).exceptionally(ex -> {
            log.error("Erro ao publicar no Kafka: {}", ex.getMessage());
            throw new MessagePublishingException("Falha ao enviar para o Kafka");
        });
    }

}
