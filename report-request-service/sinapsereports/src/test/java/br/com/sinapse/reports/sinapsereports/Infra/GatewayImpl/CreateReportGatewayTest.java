package br.com.sinapse.reports.sinapsereports.Infra.GatewayImpl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Infra.Persistence.Repository.ReportRepositoryJpa;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CreateReportGatewayTest {

    @InjectMocks
    private CreateReportGatewayImpl createReportGateway;

    @Mock
    private ReportRepositoryJpa reportRepositoryJpa;

    @Test
    void given_validReport_when_save_then_returnReport() {
        var expectedType = ReportType.PDF;
        var expectedStatus = ReportStatus.PENDING_SEND;
        var expectedDate = LocalDate.now();
        var expectedParameters = "parameters";

        var createdReport = ReportRequest.create(
                expectedStatus.name(),
                expectedType.name(),
                expectedDate,
                expectedDate,
                expectedParameters);

        when(reportRepositoryJpa.save(any())).thenReturn(any());

        assertDoesNotThrow(() -> createReportGateway.create(createdReport));
    }

    @Test
    void given_invalidReport_when_save_then_return_exception() {

        var expectedMessage = "Erro ao salvar report";

        var expectedType = ReportType.PDF;
        var expectedStatus = ReportStatus.PENDING_SEND;
        var expectedDate = LocalDate.now();
        var expectedParameters = "parameters";

        var createdReport = ReportRequest.create(
                expectedStatus.name(),
                expectedType.name(),
                expectedDate,
                expectedDate,
                expectedParameters);

        when(reportRepositoryJpa.save(any())).thenReturn(null);

        var expectedError = assertThrows(RuntimeException.class, () -> createReportGateway.create(createdReport));

        assertEquals(expectedMessage, expectedError.getMessage());
    }

}
