package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

import static br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest.create;

@ExtendWith(MockitoExtension.class)
public class GetReportUseCaseImpTest {

    @Mock
    private ReportCommandGateway reportCommandGateway;

    @InjectMocks
    private GetReportUseCaseImp getReportUseCaseImp;

    @Test
    void given_validReport_when_getReport_then_return_report() {
        var aReportRequest = create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "expectedParameters");

        var id = aReportRequest.getId().getValue();

        when(reportCommandGateway.findByID(id)).thenReturn(aReportRequest);

        var reportRequest = getReportUseCaseImp.execute(id);
        assertNotNull(reportRequest);
        assertEquals(id, reportRequest.getId().getValue());
        assertEquals(ReportStatus.PENDING_SEND, reportRequest.getStatus());
        assertEquals(ReportType.PDF, reportRequest.getReportType());
    }

    @Test
    void given_nullId_when_execute_then_throw_NullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            getReportUseCaseImp.execute(null);
        });
    }

}
