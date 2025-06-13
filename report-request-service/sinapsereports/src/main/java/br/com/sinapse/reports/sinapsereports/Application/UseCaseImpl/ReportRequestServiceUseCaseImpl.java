package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.Service.ReportPublisherService;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ReportRequestServiceUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReportRequestServiceUseCaseImpl implements ReportRequestServiceUseCase {

    private final ReportMapper reportMapper;

    private final ReportRepository reportRepository;
    private final ReportRequestServiceUseCase self;
    private final ReportPublisherService reportPublisherService;

    public ReportRequestServiceUseCaseImpl(ReportMapper reportMapper,
            ReportRepository reportRepository, @Lazy ReportRequestServiceUseCase self,
            ReportPublisherService reportPublisherService) {
        this.reportMapper = reportMapper;

        this.reportRepository = reportRepository;
        this.self = self;
        this.reportPublisherService = reportPublisherService;

    }

    private static final Logger log = LoggerFactory.getLogger(ReportRequestServiceUseCaseImpl.class);

    @Override
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "handleKafkaFailure")
    public CompletableFuture<ReportRequestResponseDto> requestNewReport(ReportRequest request) {

        reportPublisherService.updateStatus(request, ReportStatus.PENDING);

        return self.publishToKafka(request)
                .thenApplyAsync(aVoid -> reportMapper.toResponseDto(request)).exceptionally(
                        throwable -> reportMapper.toResponseDto(request));
    }

    @Override
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "handleKafkaFailure")
    public CompletableFuture<Void> publishToKafka(ReportRequest request) {
        return reportPublisherService.publish(request);
    }

    @Scheduled(fixedDelay = 5000)
    public void resendPendingRequests() {
        var pendingRequests = reportRepository.findByStatus(ReportStatus.PENDENTE_ENVIO);
        if (pendingRequests.isEmpty()) {
            return;
        }
        log.info("Iniciando reenvio de {} solicitações pendentes.", pendingRequests.size());

        for (var req : pendingRequests) {
            try {
                self.publishToKafka(req).get();
            } catch (Exception e) {
                log.error("Erro ao tentar reenviar a solicitação {}: {}", req.getId(), e.getMessage());
            }
        }
        log.info("Ciclo de reenvio de solicitações pendentes finalizado.");
    }

}