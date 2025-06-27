package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.time.LocalDate;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;

@ExtendWith(MockitoExtension.class)
public class CreateReportUseCaseImplTest {

    @Mock
    private ReportCommandGateway reportCommandGateway;

    @InjectMocks
    private CreateReportUseCaseImpl createReportUseCaseImpl;

    @Test
    void given_validReport_when_create_then_return_report() {

        var expectedType = ReportType.PDF;
        var expectedStatus = ReportStatus.PENDING_SEND;
        var expectedDate = LocalDate.now();
        var expectedParameters = "parameters";

        var dto = new CreateReportRequestDto(
                expectedStatus.name(),
                expectedType.name(),
                expectedDate,
                expectedDate,
                expectedParameters);

        var createdReport = create(
                expectedStatus.name(),
                expectedType.name(),
                expectedDate,
                expectedDate,
                expectedParameters);

        when(reportCommandGateway.create(any())).thenReturn(createdReport);

        var report = createReportUseCaseImpl.execute(dto).join();

        assertNotNull(report);
        assertNotNull(report.getId());
        assertEquals(expectedParameters, report.getParameters());
        assertEquals(expectedStatus, report.getStatus());
        assertEquals(expectedType, report.getReportType());
        assertEquals(expectedDate, report.getReportStartDate());
        assertEquals(expectedDate, report.getReportEndDate());
    }

    @Test
    void given_report_when_create_report_when_error_then_throw_exception() {

        var expectedMessage = "Error creating report";

        var dto = new CreateReportRequestDto(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "parameters");

        when(reportCommandGateway.create(any()))
                .thenThrow(new IllegalArgumentException(expectedMessage));

        var thrown = assertThrows(CompletionException.class, () -> {
            createReportUseCaseImpl.execute(dto).join();
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals(expectedMessage, cause.getMessage());
    }

    @Test
    void given_reportNull_when_create_report_then_throw_exception() {

        var expectedMessage = "Objeto não pode ser nulo";

        var thrown = assertThrows(NullPointerException.class, () -> {
            createReportUseCaseImpl.execute(null).join();
        });

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void given_valid_report_when_create_report_then_return_null_should_throw_exception() {

        var dto = new CreateReportRequestDto(
                ReportStatus.PENDING_SEND.name(),
                ReportType.PDF.name(),
                LocalDate.now(),
                LocalDate.now(),
                "parameters");

        doAnswer(
                invocation -> {
                    System.out.println("simulando retorno nulo");
                    return null;
                }).when(reportCommandGateway).create(any());

        var thrown = assertThrows(java.util.concurrent.CompletionException.class, () -> {
            createReportUseCaseImpl.execute(dto).join();
        });
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof IllegalStateException);

    }
}
