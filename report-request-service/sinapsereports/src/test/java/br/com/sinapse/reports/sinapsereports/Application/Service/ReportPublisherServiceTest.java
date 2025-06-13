package br.com.sinapse.reports.sinapsereports.Application.Service;

import br.com.sinapse.reports.sinapsereports.Application.ReportRequestFactory;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;
// Importe sua factory
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReportPublisherServiceTest {

    @MockBean
    private StreamBridge streamBridge;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ReportMapper reportMapper;

    @SpyBean
    private ReportPublisherService reportPublisherService;

    private ReportRequest reportRequest;
    private ReportRequestResponseDto reportDto;

    @BeforeEach
    void setUp() {
        // Arrange: Usando a factory para criar dados de teste consistentes.
        reportRequest = ReportRequestFactory.criarReportRequestPdfEngenharia();

        reportDto = new ReportRequestResponseDto(
                reportRequest.getId(),
                ReportStatus.PENDING.name(),
                "Request received");

        when(reportMapper.toResponseDto(any(ReportRequest.class))).thenReturn(reportDto);
    }

    @Test
    @DisplayName("Deve publicar com sucesso e atualizar status para PENDING")
    void given_successful_send_when_publish_then_status_is_updated_to_pending() throws Exception {
        // When
        reportPublisherService.publish(reportRequest).get();

        // Then
        ArgumentCaptor<ReportRequest> requestCaptor = ArgumentCaptor.forClass(ReportRequest.class);
        verify(streamBridge, times(1)).send(eq("publishReportRequest-out-0"), eq(reportDto));
        verify(reportRepository, times(1)).save(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getStatus()).isEqualTo(ReportStatus.PENDING);
        assertThat(requestCaptor.getValue().getId()).isEqualTo(reportRequest.getId());
    }

    @Test
    @DisplayName("Deve acionar o fallback quando a publicação no Kafka falhar")
    void given_kafka_failure_when_publish_then_fallback_is_triggered() {

        doThrow(new RuntimeException("Kafka is offline")).when(streamBridge).send(anyString(), any());

        reportPublisherService.publish(reportRequest);

        ArgumentCaptor<ReportRequest> captor = ArgumentCaptor.forClass(ReportRequest.class);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(reportPublisherService, times(1)).handlePublishFailure(any(ReportRequest.class),
                    any(Throwable.class));
            verify(reportRepository, times(1)).save(captor.capture());
        });

        assertThat(captor.getValue().getStatus()).isEqualTo(ReportStatus.PENDENTE_ENVIO);
    }

    @Test
    @DisplayName("NÃO deve acionar o fallback quando a publicação for bem-sucedida")
    void given_successful_send_when_publish_then_fallback_is_not_invoked() throws Exception {
        // When
        reportPublisherService.publish(reportRequest).get();

        // Then
        verify(reportPublisherService, never()).handlePublishFailure(any(), any());
    }

    @Test
    @DisplayName("Deve atualizar o status e salvar no repositório corretamente")
    void given_report_and_status_when_update_status_then_repository_saves_correct_state() {
        // When
        reportPublisherService.updateStatus(reportRequest, ReportStatus.PENDING);

        // Then
        ArgumentCaptor<ReportRequest> captor = ArgumentCaptor.forClass(ReportRequest.class);
        verify(reportRepository).save(captor.capture());

        assertThat(captor.getValue().getStatus()).isEqualTo(ReportStatus.PENDING);
    }

    @Test
    @DisplayName("Deve atualizar status para PENDENTE_ENVIO ao chamar o fallback diretamente")
    void given_report_and_error_when_handle_publish_failure_is_called_directly_then_status_is_pendente_envio() {
        // Given
        Throwable error = new RuntimeException("Simulated error");

        // When
        reportPublisherService.handlePublishFailure(reportRequest, error);

        // Then
        ArgumentCaptor<ReportRequest> captor = ArgumentCaptor.forClass(ReportRequest.class);
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(reportRepository).save(captor.capture()));
        assertThat(captor.getValue().getStatus()).isEqualTo(ReportStatus.PENDENTE_ENVIO);
    }

    @Test
    @DisplayName("Deve lançar exceção se o repositório falhar durante uma publicação bem-sucedida")
    void given_successful_send_but_repository_fails_when_publish_then_exception_is_thrown() {
        // Given
        doThrow(new RuntimeException("DB is down")).when(reportRepository).save(any());

        // When / Then
        assertThatThrownBy(() -> reportPublisherService.publish(reportRequest).get())
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB is down");

        verify(streamBridge).send(anyString(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção se o repositório falhar durante o acionamento do fallback")
    void given_kafka_failure_and_repository_fails_when_fallback_is_triggered_then_exception_is_thrown() {
        // Given
        doThrow(new RuntimeException("Kafka down")).when(streamBridge).send(anyString(), any());
        doThrow(new RuntimeException("DB is down")).when(reportRepository).save(any());

        // When / Then
        // A exceção do repositório vai vazar da thread do @Async
        assertThatThrownBy(() -> reportPublisherService.publish(reportRequest).get());

        // Verificamos que o fallback foi chamado, mesmo que tenha falhado internamente.
        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(reportPublisherService).handlePublishFailure(any(), any()));
    }

    @Test
    @DisplayName("Deve completar o Future com exceção se o ReportRequest for nulo")
    void given_null_report_request_when_publish_then_future_completes_exceptionally() {
        // When
        CompletableFuture<Void> future = reportPublisherService.publish(null);

        // Then
        assertThat(future).isCompletedExceptionally();

        assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);

        verify(streamBridge, never()).send(any(), any());
        verify(reportRepository, never()).save(any());
    }
}