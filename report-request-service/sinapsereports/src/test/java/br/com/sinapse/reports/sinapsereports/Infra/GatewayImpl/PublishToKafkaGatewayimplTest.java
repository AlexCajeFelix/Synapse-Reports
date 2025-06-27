package br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cloud.stream.function.StreamBridge;

import br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Fallback.FallBackPublishToKafkaUseCaseUseCaseImp;

import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

public class PublishToKafkaGatewayimplTest {

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private FallBackPublishToKafkaUseCaseUseCaseImp fallbackUseCase;

    @InjectMocks
    private PublishToKafkaGatewayimpl publishGateway;

    @Test
    void when_sendToKafkaFallback_called_then_fallbackUseCase_executeIsCalled() {
        ReportRequest report = mock(ReportRequest.class);
        Throwable someError = new RuntimeException("some error");

        publishGateway.sendToKafkaFallback(report, someError);

        verify(fallbackUseCase, times(1)).execute(report);
    }
}
