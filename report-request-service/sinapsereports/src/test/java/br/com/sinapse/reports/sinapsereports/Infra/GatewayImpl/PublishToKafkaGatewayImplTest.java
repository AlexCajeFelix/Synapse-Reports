package br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportType;

@ExtendWith(MockitoExtension.class)
public class PublishToKafkaGatewayImplTest {

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private PublishToKafkaGatewayimpl publishGateway;

    @BeforeEach
    public void setup()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        var field = PublishToKafkaGatewayimpl.class.getDeclaredField("channelName");
        field.setAccessible(true);
        field.set(publishGateway, "report-out-0");
    }

    @Test
    void given_valid_Report_when_send_to_kafka_then_should_not_throw_exception() {

        var aReportRequest = ReportRequest.create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "expectedParameters");

        when(streamBridge.send(anyString(), any())).thenReturn(true);

        assertDoesNotThrow(() -> publishGateway.publishReport(aReportRequest));
    }

    @Test
    void given_invalid_report_when_send_to_kafka_then_should_throw_exception() {
        var aReportRequest = ReportRequest.create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "expectedParameters");
        var id = aReportRequest.getId().getValue();

        when(streamBridge.send(anyString(), any())).thenReturn(false);

        var expectedError = assertThrows(MessagePublishingException.class,
                () -> publishGateway.publishReport(aReportRequest));

        assertEquals("Kafka não aceitou a mensagem: " + id, expectedError.getMessage());
    }

}
