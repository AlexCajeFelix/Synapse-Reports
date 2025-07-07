package br.com.sinapse.reports.sinapsereports.application.report.usecaseimpl;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest.create;

@ExtendWith(MockitoExtension.class)
public class ManageStatusUseCaseImplTest {

    @InjectMocks
    private ManageStatusUseCaseImpl manageStatusUseCaseImpl;

    @Mock
    private ReportCommandGateway reportCommandGateway;

    @Test
    void given_nullStatus_when_execute_then_throwNullPointerException() {
        var report = create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "params");

        var thrown = assertThrows(NullPointerException.class, () -> {
            manageStatusUseCaseImpl.execute(report, null);
        });

        assertEquals("status não pode ser nulo", thrown.getMessage());
    }

    @Test
    void given_nullReport_when_execute_then_throwNullPointerException() {
        var thrown = assertThrows(NullPointerException.class, () -> {
            manageStatusUseCaseImpl.execute(null, ReportStatus.PENDING);
        });

        assertEquals("report não pode ser nulo", thrown.getMessage());
    }

    @Test
    void given_validReport_when_execute_then_updateCalledOnce() {
        var report = create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "params");

        doNothing().when(reportCommandGateway).update(report, ReportStatus.PENDING);

        manageStatusUseCaseImpl.execute(report, ReportStatus.PENDING);

        verify(reportCommandGateway, times(1)).update(report, ReportStatus.PENDING);
    }

    @Test
    void given_gatewayThrowsException_when_execute_then_propagateException() {
        var report = create(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "params");

        doThrow(new RuntimeException("DB error")).when(reportCommandGateway).update(report, ReportStatus.PENDING);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            manageStatusUseCaseImpl.execute(report, ReportStatus.PENDING);
        });

        assertEquals("DB error", thrown.getMessage());
    }

}