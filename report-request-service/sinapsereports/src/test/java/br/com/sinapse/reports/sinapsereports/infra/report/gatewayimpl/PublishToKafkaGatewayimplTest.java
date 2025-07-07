package br.com.sinapse.reports.sinapsereports.infra.report.gatewayimpl;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.infra.report.fallback.FallbackGatewayImpl;
import br.com.sinapse.reports.sinapsereports.infra.report.mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.entitiesjpa.ReportRequestEntity;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository.ReportRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.cloud.stream.function.StreamBridge;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest.create;

class PublishToKafkaGatewayimplTest {

    private StreamBridge streamBridge;
    private FallbackGatewayImpl fallbackGateway;
    private ReportRepositoryJpa reportRepository;
    private PublishToKafkaGatewayimpl gateway;

    @BeforeEach
    void setUp() {
        streamBridge = mock(StreamBridge.class);
        fallbackGateway = mock(FallbackGatewayImpl.class);
        reportRepository = mock(ReportRepositoryJpa.class);
        gateway = new PublishToKafkaGatewayimpl(streamBridge, fallbackGateway, reportRepository);

        try {
            var field = PublishToKafkaGatewayimpl.class.getDeclaredField("channelName");
            field.setAccessible(true);
            field.set(gateway, "test-channel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Deve publicar relatório no Kafka com sucesso")
    void testPublishReportSuccess() {
        var createdReport = create(
                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

        doReturn(true).when(streamBridge).send("test-channel", createdReport);

        assertDoesNotThrow(() -> gateway.publishReport(createdReport));
        verify(streamBridge).send("test-channel", createdReport);
    }

    @Test
    @DisplayName("Deve lançar exceção ao publicar relatório se Kafka não aceitar mensagem")
    void testPublishReportKafkaFails() {
        var createdReport = create(
                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

        doReturn(false).when(streamBridge).send("test-channel", createdReport);

        MessagePublishingException ex = assertThrows(MessagePublishingException.class,
                () -> gateway.publishReport(createdReport));
        assertTrue(ex.getMessage().contains("Kafka não aceitou a mensagem"));
    }

    @Test
    @DisplayName("Deve chamar fallback ao acionar sendToKafkaFallback")
    void testSendToKafkaFallback() {
        var createdReport = create(
                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");
        Throwable throwable = new RuntimeException("erro");

        gateway.sendToKafkaFallback(createdReport, throwable);

        verify(fallbackGateway).publishReport(createdReport);
    }

    @Test
    @DisplayName("Deve reenviar relatórios pendentes com sucesso")
    void testResendPendingRequests() {
        ReportRequestEntity entity1 = mock(ReportRequestEntity.class);
        ReportRequestEntity entity2 = mock(ReportRequestEntity.class);
        List<ReportRequestEntity> pendingEntities = List.of(entity1, entity2);

        when(reportRepository.findByStatus(ReportStatus.FAILED.name())).thenReturn(pendingEntities);

        var domain1 = create("PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters1");
        var domain2 = create("PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters2");

        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toDomain(entity1)).thenReturn(domain1);
            mockedMapper.when(() -> ReportMapper.toDomain(entity2)).thenReturn(domain2);

            doReturn(true).when(streamBridge).send("test-channel", domain1);
            doReturn(true).when(streamBridge).send("test-channel", domain2);

            assertDoesNotThrow(() -> gateway.resendPendingRequests());
            verify(streamBridge).send("test-channel", domain1);
            verify(streamBridge).send("test-channel", domain2);
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao reenviar pendentes se Kafka não aceitar mensagem")
    void testResendPendingRequestsKafkaFails() {
        ReportRequestEntity entity = mock(ReportRequestEntity.class);
        when(reportRepository.findByStatus(ReportStatus.FAILED.name())).thenReturn(List.of(entity));
        var domain = create("PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toDomain(entity)).thenReturn(domain);
            doReturn(false).when(streamBridge).send("test-channel", domain);

            MessagePublishingException ex = assertThrows(MessagePublishingException.class,
                    () -> gateway.resendPendingRequests());
            assertTrue(ex.getMessage().contains("Kafka não aceitou a mensagem"));
            verify(streamBridge).send("test-channel", domain);
        }
    }
}