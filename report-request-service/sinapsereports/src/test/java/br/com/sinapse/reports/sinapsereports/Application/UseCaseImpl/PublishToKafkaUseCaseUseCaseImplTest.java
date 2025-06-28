package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl.PublishToKafkaGatewayimpl;

@ExtendWith(MockitoExtension.class)
public class PublishToKafkaUseCaseUseCaseImplTest {

    @InjectMocks
    private PublishToKafkaUseCaseUseCaseImpl publishToKafkaUseCaseUseCaseImpl;

    @Mock
    private PublishToKafkaGatewayimpl publishToKafkaGatewayimpl;

    @Test
    void given_valid_report_when_publish_to_kafka_then_not_should_throw_exception() {

        var aReportRequest = ReportRequest.create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "expectedParameters");

        doNothing().when(publishToKafkaGatewayimpl).publishReport(aReportRequest);

        assertDoesNotThrow(() -> publishToKafkaUseCaseUseCaseImpl.execute(aReportRequest));

    }

    @Test
    void given_null_report_when_publish_to_kafka_then_should_throw_exception() {
        var exception = assertThrows(NullPointerException.class, () -> {
            publishToKafkaUseCaseUseCaseImpl.execute(null);
        });

        assertEquals("Objeto nao pode ser nulo", exception.getMessage());
    }

}
