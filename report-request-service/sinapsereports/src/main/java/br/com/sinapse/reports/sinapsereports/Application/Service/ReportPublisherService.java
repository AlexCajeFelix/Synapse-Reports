package br.com.sinapse.reports.sinapsereports.Application.Service;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ReportPublisherService {

    private static final Logger log = LoggerFactory.getLogger(ReportPublisherService.class);
    private static final String PRODUCER_BINDING_NAME = "publishReportRequest-out-0";
    private static final String CIRCUIT_BREAKER_NAME = "publish-kafka";

    private final StreamBridge streamBridge;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportPublisherService(StreamBridge streamBridge, ReportRepository reportRepository,
            ReportMapper reportMapper) {
        this.streamBridge = streamBridge;
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "handlePublishFailure")
    @Async("reportTaskExecutor")
    public CompletableFuture<Void> publish(ReportRequest event) {

        if (event == null) {
            log.error("Solicitação nula. Nenhuma publicação realizada.");
            throw new MessagePublishingException("Solicitação nula. Nenhuma publicação realizada.");
        }

        ReportRequestResponseDto dto = reportMapper.toResponseDto(event);

        boolean sent = streamBridge.send(PRODUCER_BINDING_NAME, dto);
        if (!sent) {
            throw new MessagePublishingException("Falha ao enviar mensagem para Kafka");
        }

        log.info("Publicação da solicitação {} bem-sucedida.", event.getId());
        updateStatus(event, ReportStatus.PENDING);
        return CompletableFuture.completedFuture(null);
    }

    @Async("reportTaskExecutor")
    public CompletableFuture<Void> handlePublishFailure(ReportRequest event, Throwable e) {
        if (event == null) {
            throw new MessagePublishingException("Solicitação nula. Nenhuma publicação realizada.");
        }
        log.error("FALLBACK ACIONADO para a solicitação {}. Causa: {}", event.getId(), e.getMessage());
        updateStatus(event, ReportStatus.PENDENTE_ENVIO);
        return CompletableFuture.completedFuture(null);
    }

    public void updateStatus(ReportRequest request, ReportStatus status) {
        if (request == null || status == null) {
            throw new MessagePublishingException("Solicitação nula. Nenhuma publicação realizada.");
        }
        request.setStatus(status);
        reportRepository.save(request);
        log.info("Status da solicitação {} atualizado para {}", request.getId(), status);
    }
}