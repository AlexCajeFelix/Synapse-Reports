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
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
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
    @CircuitBreaker(name = "publish-kafka", fallbackMethod = "sendToKafkaFallback")
    public void execute(final ReportRequest aReport) {
        try {
            sendToKafka(aReport);

        } catch (Exception e) {
            throw new MessagePublishingException("Erro ao publicar a solicitação: " + aReport.getId());
        }

    }

    @Async("reportTaskExecutor")
    public void sendToKafka(final ReportRequest reportRequest) {
        var eventDto = reportMapper.toEventDto(reportRequest);
        boolean sent;

        sent = aStreamBridge.send(channelName, eventDto);
        if (!sent) {
            throw new MessagePublishingException("Kafka não aceitou a mensagem: " + reportRequest.getId());
        }

        log.info("Solicitação {} publicada com sucesso.", reportRequest.getId());
        manageStatusUseCase.execute(reportRequest, ReportStatus.PENDING);
    }

    @Async("reportTaskExecutor")
    public void sendToKafkaFallback(final ReportRequest aReport, final Throwable e) {
        log.error("Erro ao publicar a solicitação: {}", aReport.getId(), e);

        fallBackPublishToKafkaUseCaseUseCaseImp.execute(aReport);
    }

    @Scheduled(fixedDelayString = "${my.scheduler.delay}")
    public void resendPendingRequests() {
        var pendingRequests = reportRepository.findByStatus(ReportStatus.FAILED);

        if (pendingRequests.isEmpty()) {
            log.info("Nenhuma solicitação pendente para reenvio.");
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
