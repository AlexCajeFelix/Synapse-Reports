package br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Fallback.FallBackPublishToKafkaUseCaseUseCaseImp;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.PublishReportCommandGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PublishToKafkaGatewayimpl implements PublishReportCommandGateway {

    private final StreamBridge aStreamBridge;

    private final FallBackPublishToKafkaUseCaseUseCaseImp fallBackPublishToKafkaUseCaseUseCaseImp;

    public PublishToKafkaGatewayimpl(final StreamBridge aStreamBridge,
            final FallBackPublishToKafkaUseCaseUseCaseImp fallBackPublishToKafkaUseCaseUseCaseImp) {

        this.aStreamBridge = aStreamBridge;
        this.fallBackPublishToKafkaUseCaseUseCaseImp = fallBackPublishToKafkaUseCaseUseCaseImp;

    }

    @Value("${channel}")
    private String channelName;

    @Override
    @CircuitBreaker(name = "publish-kafka", fallbackMethod = "sendToKafkaFallback")
    public void publishReport(ReportRequest reportRequest) {
        try {
            sendToKafka(reportRequest);

        } catch (Exception e) {
            throw new MessagePublishingException("Erro ao publicar a solicitação: ");
        }
    }

    public void sendToKafka(final ReportRequest reportRequest) {

        boolean sent;

        sent = aStreamBridge.send(channelName, reportRequest);
        if (!sent) {
            throw new MessagePublishingException("Kafka não aceitou a mensagem: " + reportRequest.getId());
        }

    }

    public void sendToKafkaFallback(final ReportRequest aReport, final Throwable e) {

        fallBackPublishToKafkaUseCaseUseCaseImp.execute(aReport);
    }

    /*
     * @Scheduled(fixedDelayString = "${my.scheduler.delay}")
     * public void resendPendingRequests() {
     * var pendingRequests = reportRepository.findByStatus(ReportStatus.FAILED);
     * 
     * if (pendingRequests.isEmpty()) {
     * log.info("Nenhuma solicitação pendente para reenvio.");
     * return;
     * }
     * 
     * log.info("Iniciando reenvio de {} solicitações pendentes.",
     * pendingRequests.size());
     * 
     * for (var req : pendingRequests) {
     * try {
     * sendToKafka(req);
     * } catch (Exception e) {
     * log.error("Erro ao tentar reenviar a solicitação {}: {}", req.getId(),
     * e.getMessage());
     * }
     * }
     * log.info("Ciclo de reenvio de solicitações pendentes finalizado.");
     * }
     */

}
