package br.com.sinapse.reports.sinapsereports.infra.report.gatewayimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.PublishReportCommandGateway;
import br.com.sinapse.reports.sinapsereports.infra.report.fallback.FallbackGatewayImpl;
import br.com.sinapse.reports.sinapsereports.infra.report.mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository.ReportRepositoryJpa;
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
