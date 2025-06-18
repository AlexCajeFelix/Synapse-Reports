package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Fallback.FallBackPublishToKafkaUseCaseUseCaseImp;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PublishToKafkaUseCaseUseCaseImp extends PublishToKafkaUseCase {

    private final StreamBridge aStreamBridge;
    private final ReportMapper reportMapper;
    private static final Logger log = LoggerFactory.getLogger(PublishToKafkaUseCaseUseCaseImp.class);
    private final FallBackPublishToKafkaUseCaseUseCaseImp fallBackPublishToKafkaUseCaseUseCaseImp;
    private final ManageStatusUseCaseImpl manageStatusUseCase;
    private final ReportRepository reportRepository;
    private final ReportStatus PENDENTE_ENVIO = ReportStatus.PENDENTE_ENVIO;

    @Value("${channel}")
    private String channelName;

    public PublishToKafkaUseCaseUseCaseImp(final StreamBridge aStreamBridge, final ReportMapper reportMapper,
            final FallBackPublishToKafkaUseCaseUseCaseImp fallBackPublishToKafkaUseCaseUseCaseImp,
            final ManageStatusUseCaseImpl manageStatusUseCase, final ReportRepository reportRepository) {
        this.reportMapper = reportMapper;
        this.aStreamBridge = aStreamBridge;
        this.fallBackPublishToKafkaUseCaseUseCaseImp = fallBackPublishToKafkaUseCaseUseCaseImp;
        this.manageStatusUseCase = manageStatusUseCase;
        this.reportRepository = reportRepository;
    }

    @Override
    @Async("reportTaskExecutor")
    @CircuitBreaker(name = "publish-kafka", fallbackMethod = "sendToKafkaFallback")
    public void execute(final ReportRequest aReport) {
        try {
            sendToKafka(aReport);
            manageStatusUseCase.execute(aReport, ReportStatus.PENDING);

        } catch (Exception e) {

            System.out.println("deu errroooooo");
        }

    }

    @Async("reportTaskExecutor")

    public void sendToKafka(final ReportRequest reportRequest) {
        var eventDto = reportMapper.toEventDto(reportRequest);

        aStreamBridge.send(channelName, eventDto);
        log.info("Solicitação {} publicada com sucesso.", reportRequest.getId());

    }

    @Async("reportTaskExecutor")
    public void sendToKafkaFallback(final ReportRequest aReport, final Throwable e) {
        System.out.println("deu errroooooo");

        fallBackPublishToKafkaUseCaseUseCaseImp.execute(aReport);
    }

    @Scheduled(fixedDelayString = "${my.scheduler.delay}")
    @Async("reportTaskExecutor")
    public void resendPendingRequests() {
        var pendingRequests = reportRepository.findByStatus(PENDENTE_ENVIO);

        if (pendingRequests.isEmpty()) {
            return;
        }

        log.info("Iniciando reenvio de {} solicitações pendentes.", pendingRequests.size());

        for (var req : pendingRequests) {
            try {
                sendToKafka(req);
            } catch (Exception e) {
                log.error("Erro ao tentar reenviar a solicitação {}: {}", req.getId(), e.getMessage());
            }
        }
        log.info("Ciclo de reenvio de solicitações pendentes finalizado.");
    }

}
