package br.com.sinapse.reports.sinapsereports.Application.Service;

import br.com.sinapse.reports.sinapsereports.Application.ReportRequestFactory;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

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
@TestPropertySource(properties = "my.scheduler.delay=1000")
@ActiveProfiles("test")
class ReportPublisherServiceTest {

    @MockBean
    private StreamBridge stream_bridge;
    @MockBean
    private ReportRepository report_repository;
    @MockBean
    private ReportMapper report_mapper;

    @Autowired
    private ReportPublisherService report_publisher_service;

    @Autowired
    private CircuitBreakerRegistry circuit_breaker_registry;

    private ReportRequest report_request;

    @BeforeEach
    void set_up() {
        reset(stream_bridge, report_repository, report_mapper);
        circuit_breaker_registry.circuitBreaker("publish-kafka").reset();

        this.report_request = ReportRequestFactory.create_pdf_report_request_engineering();
        when(report_mapper.toResponseDto(any(ReportRequest.class))).thenReturn(null);
        when(report_repository.save(any(ReportRequest.class))).thenReturn(report_request);
    }

    @Test
    @DisplayName("given valid request when publish then save entity with pending status")
    void given_valid_request_when_publish_then_save_entity_with_pending_status() {
        when(stream_bridge.send(anyString(), any())).thenReturn(true);

        report_publisher_service.publish(report_request);

        verify(stream_bridge, timeout(1000).times(1)).send(eq("publishReportRequest-out-0"), any());

        ArgumentCaptor<ReportRequest> captor = ArgumentCaptor.forClass(ReportRequest.class);
        verify(report_repository, timeout(1000).times(1)).save(captor.capture());

        ReportRequest saved_request = captor.getValue();
        assertThat(saved_request.getStatus()).isEqualTo(ReportStatus.PENDING);
    }

    @Test
    @DisplayName("given kafka failure when publish then save entity with pending_send status")
    void given_kafka_failure_when_publish_then_save_entity_with_pending_send_status() {
        doThrow(new RuntimeException("Kafka is offline")).when(stream_bridge).send(anyString(), any());

        report_publisher_service.publish(report_request);

        ArgumentCaptor<ReportRequest> captor = ArgumentCaptor.forClass(ReportRequest.class);

        verify(report_repository, timeout(2000).times(1)).save(captor.capture());

        ReportRequest saved_request = captor.getValue();
        assertThat(saved_request.getStatus()).isEqualTo(ReportStatus.PENDENTE_ENVIO);
    }

    @Test
    @DisplayName("given repository failure on success path when publish then throw exception")
    void given_repository_failure_on_success_path_when_publish_then_throw_exception() {
        when(stream_bridge.send(anyString(), any())).thenReturn(true);
        doThrow(new RuntimeException("DB is down")).when(report_repository).save(any(ReportRequest.class));

        assertThatThrownBy(() -> report_publisher_service.publish(report_request).get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB is down");

        verify(stream_bridge, timeout(1000).times(1)).send(eq("publishReportRequest-out-0"), any());
    }

    @Test
    @DisplayName("given null request when publish then throw message publishing exception async")
    void given_null_request_when_publish_then_throw_message_publishing_exception_async() {
        CompletableFuture<Void> future = report_publisher_service.publish(null);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(future).isCompletedExceptionally();
        });

        assertThatThrownBy(() -> future.join())
                .hasCauseInstanceOf(MessagePublishingException.class)
                .hasRootCauseMessage("Solicitação nula. Nenhuma publicação realizada.");

        verifyNoInteractions(stream_bridge, report_repository);
    }

    @Test
    @DisplayName("given valid status when update_status then save entity with correct status")
    void given_valid_status_when_update_status_then_save_entity_with_correct_status() {
        ReportStatus new_status = ReportStatus.COMPLETED;

        report_publisher_service.updateStatus(report_request, new_status);

        ArgumentCaptor<ReportRequest> captor = ArgumentCaptor.forClass(ReportRequest.class);
        verify(report_repository).save(captor.capture());

        ReportRequest saved_request = captor.getValue();
        assertThat(saved_request.getStatus()).isEqualTo(new_status);
    }
}
