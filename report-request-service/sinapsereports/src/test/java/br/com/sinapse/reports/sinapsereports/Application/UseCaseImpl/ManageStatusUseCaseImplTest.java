package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

@ExtendWith(MockitoExtension.class)
public class ManageStatusUseCaseImplTest {

    @InjectMocks
    private ManageStatusUseCaseImpl manageStatusUseCaseImpl;

    @Mock
    private ReportCommandGateway reportCommandGateway;

    @Test
    void given_validReport_when_getReport_then_return_report() {
        var aReportRequest = create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "expectedParameters");

        var id = aReportRequest.getId().getValue();

        assertEquals(ReportStatus.PENDING_SEND, aReportRequest.getStatus());

        aReportRequest.updateStatus(ReportStatus.PENDING.name());

        assertEquals(id, aReportRequest.getId().getValue());
        assertEquals(ReportStatus.PENDING, aReportRequest.getStatus());
        assertEquals(ReportType.PDF, aReportRequest.getReportType());

        doNothing().when(reportCommandGateway).update(aReportRequest, ReportStatus.PENDING_SEND);

    }
}
