package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.util.ReflectionTestUtils;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestedEvent;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Fallback.FallBackPublishToKafkaUseCaseUseCaseImp;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;
import static br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Factory.ReportRequestFactory.createValidReportRequest;

@ExtendWith(MockitoExtension.class)
public class PublishToKafkaUseCaseUseCaseImpTest {

    private static final String CHANNEL = "fake-channel";

    private PublishToKafkaUseCaseUseCaseImp publishToKafkaUseCase;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private FallBackPublishToKafkaUseCaseUseCaseImp fallback;

    @Mock
    private ManageStatusUseCaseImpl manageStatusUseCase;

    @Mock
    private ReportRepository reportRepository;

    @BeforeEach
    public void setUp() {
        PublishToKafkaUseCaseUseCaseImp instance = new PublishToKafkaUseCaseUseCaseImp(
                streamBridge,
                reportMapper,
                fallback,
                manageStatusUseCase,
                reportRepository);

        publishToKafkaUseCase = spy(instance);
        ReflectionTestUtils.setField(publishToKafkaUseCase, "channelName", CHANNEL);
    }

    @Test
    public void given_validReport_when_execute_then_shouldSendToKafkaAndSetPendingStatus() {
        ReportRequest report = createValidReportRequest();
        ReportRequestedEvent eventDto = new ReportRequestedEvent(
                report.getId(),
                report.getReportType(),
                report.getStatus(),
                report.getReportStartDate(),
                report.getReportEndDate(),
                report.getParameters(),
                report.getRequestedAt());

        when(reportMapper.toEventDto(report)).thenReturn(eventDto);
        when(streamBridge.send(anyString(), any())).thenReturn(true);

        publishToKafkaUseCase.execute(report);

        verify(streamBridge).send(eq(CHANNEL), eq(eventDto));
        verify(manageStatusUseCase).execute(report, ReportStatus.PENDING);
    }

    @Test
    public void given_streamBridgeFails_when_execute_then_shouldThrowException() {
        ReportRequest report = createValidReportRequest();
        ReportRequestedEvent eventDto = new ReportRequestedEvent(
                report.getId(),
                report.getReportType(),
                report.getStatus(),
                report.getReportStartDate(),
                report.getReportEndDate(),
                report.getParameters(),
                report.getRequestedAt());

        when(reportMapper.toEventDto(report)).thenReturn(eventDto);
        when(streamBridge.send(CHANNEL, eventDto)).thenReturn(false);

        MessagePublishingException exception = assertThrows(
                MessagePublishingException.class,
                () -> publishToKafkaUseCase.execute(report));

        assertEquals("Erro ao publicar a solicitação: " + report.getId(), exception.getMessage());
    }

    @Test
    public void given_noFailedReports_when_resendPendingRequests_then_shouldDoNothing() {
        when(reportRepository.findByStatus(ReportStatus.FAILED)).thenReturn(Collections.emptyList());

        publishToKafkaUseCase.resendPendingRequests();

        verify(publishToKafkaUseCase, never()).sendToKafka(any());
    }

    @Test
    public void given_repositoryThrowsException_when_resendPendingRequests_then_shouldThrowException() {
        RuntimeException dbException = new RuntimeException("Simulação de falha no banco de dados");
        when(reportRepository.findByStatus(ReportStatus.FAILED)).thenThrow(dbException);

        assertThrows(RuntimeException.class, () -> publishToKafkaUseCase.resendPendingRequests());

        verify(publishToKafkaUseCase, never()).sendToKafka(any());
    }

    @Test
    public void given_singleFailedReport_when_resendPendingRequests_then_shouldResend() {
        ReportRequest report = createValidReportRequest();
        when(reportRepository.findByStatus(ReportStatus.FAILED)).thenReturn(Collections.singletonList(report));

        publishToKafkaUseCase.resendPendingRequests();

        verify(publishToKafkaUseCase).sendToKafka(report);
    }

    @Test
    public void given_multipleFailedReports_when_oneFails_then_shouldContinueProcessingOthers() {
        ReportRequest report1 = createValidReportRequest();
        ReportRequest report2 = createValidReportRequest();

        when(reportRepository.findByStatus(ReportStatus.FAILED)).thenReturn(List.of(report1, report2));

        doThrow(new RuntimeException("Failure")).when(publishToKafkaUseCase).sendToKafka(eq(report1));
        doNothing().when(publishToKafkaUseCase).sendToKafka(eq(report2));

        publishToKafkaUseCase.resendPendingRequests();

        verify(publishToKafkaUseCase).sendToKafka(report1);
        verify(publishToKafkaUseCase).sendToKafka(report2);
    }

    @Test
    public void given_multipleReports_when_resendPendingRequests_then_shouldCallSendForEach() {
        ReportRequest report1 = createValidReportRequest();
        ReportRequest report2 = createValidReportRequest();

        when(reportRepository.findByStatus(ReportStatus.FAILED)).thenReturn(List.of(report1, report2));

        publishToKafkaUseCase.resendPendingRequests();

        verify(publishToKafkaUseCase).sendToKafka(report1);
        verify(publishToKafkaUseCase).sendToKafka(report2);
    }

    @Test
    void given_valid_report_when_send_to_kafka_then_return_true() {
        var report = createValidReportRequest();
        var reportEvent = new ReportRequestedEvent(
                report.getId(),
                report.getReportType(),
                report.getStatus(),
                report.getReportStartDate(),
                report.getReportEndDate(),
                report.getParameters(),
                report.getRequestedAt());

        when(reportMapper.toEventDto(report)).thenReturn(reportEvent);
        when(streamBridge.send(any(), any())).thenReturn(true);
        doNothing().when(manageStatusUseCase).execute(any(), any());

        assertDoesNotThrow(() -> publishToKafkaUseCase.sendToKafka(report));
    }
}
