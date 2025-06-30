package br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.PublishReportCommandGateway;
import br.com.sinapse.reports.sinapsereports.Infra.Fallback.FallbackGatewayImpl;
import br.com.sinapse.reports.sinapsereports.Infra.Mappers.ReportMapper;

import br.com.sinapse.reports.sinapsereports.Infra.Persistence.Repository.ReportRepositoryJpa;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PublishToKafkaGatewayimpl implements PublishReportCommandGateway {

    private final StreamBridge aStreamBridge;

    private final FallbackGatewayImpl fallbackGateway;

    private final ReportRepositoryJpa reportRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public PublishToKafkaGatewayimpl(final StreamBridge aStreamBridge, final FallbackGatewayImpl fallbackGateway,
            final ReportRepositoryJpa reportRepository) {
        this.reportRepository = reportRepository;
        this.fallbackGateway = fallbackGateway;
        this.aStreamBridge = aStreamBridge;
    }

    @Value("${channel}")
    private String channelName;

    @Override
    @Async("reportTaskExecutor")
    @CircuitBreaker(name = "publish-kafka", fallbackMethod = "sendToKafkaFallback")

    public void publishReport(ReportRequest reportRequest) {
        sendToKafka(reportRequest);

        log.info("publicado no kafka thread nome da thrad que esta publicada " + Thread.currentThread().getName());
    }

    public void sendToKafka(final ReportRequest reportRequest) {

        boolean sent = aStreamBridge.send(channelName, reportRequest);
        if (!sent) {
            throw new MessagePublishingException("Kafka não aceitou a mensagem: " +
                    reportRequest.getId().getValue());
        }

        reportRequest.updateStatus(ReportStatus.PENDING.name());
        reportRepository.save(ReportMapper.toEntity(reportRequest));

        /* throw new IllegalArgumentException("SIMULANDO ERROR"); */
    }

    public void sendToKafkaFallback(final ReportRequest aReport, final Throwable e) {
        fallbackGateway.publishReport(aReport);
    }

    @Scheduled(fixedDelayString = "${my.scheduler.delay}")
    public void resendPendingRequests() {

        var pendingReports = reportRepository.findByStatus(ReportStatus.FAILED.name());
        log.info("Pendentes para reenvio: " + pendingReports.size());
        for (var report : pendingReports) {
            var reportEntityDomain = ReportMapper.toDomain(report);
            sendToKafka(reportEntityDomain);
        }

    }

}
