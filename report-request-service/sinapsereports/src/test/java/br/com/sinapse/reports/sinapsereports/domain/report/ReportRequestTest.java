package br.com.sinapse.reports.sinapsereports.domain.report;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;
import static br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest.create;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception.ReportRequestInvalidException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ReportRequestTest {

    @Test
    void given_aValidReportRequest_when_instanciate_then_shouldCreate() {

        var expectedDate = LocalDate.now();
        var expectedReportStatus = ReportStatus.PENDING_SEND;
        var expectedReportType = ReportType.PDF;
        var expectedParameters = "parameters";

        var aReportRequest = create(expectedReportStatus.name(), expectedReportType.name(), expectedDate,
                expectedDate, expectedParameters);

        var expectedId = aReportRequest.getId().getValue();

        assertNotNull(aReportRequest.getId());
        assertEquals(expectedId, aReportRequest.getId().getValue());
        assertEquals(expectedReportStatus, aReportRequest.getStatus());
        assertEquals(expectedReportType, aReportRequest.getReportType());
        assertEquals(expectedDate, aReportRequest.getReportStartDate());
        assertEquals(expectedDate, aReportRequest.getReportEndDate());
        assertEquals(expectedParameters, aReportRequest.getParameters());

    }

    @Test
    void given_aInvalidReportRequest_when_instanciate_then_shouldThrowException() {

        var expectedStatus = "Status do relatorio invalido";

        var exception = assertThrows(ReportRequestInvalidException.class,
                () -> create(null, null, null, null, null));

        assertEquals(expectedStatus, exception.getMessage());

    }

    @Test
    void given_aInvalidType_when_instanciate_then_shouldThrowException() {

        var expectedType = "Tipo do relatorio invalido";

        var exception = assertThrows(ReportRequestInvalidException.class,
                () -> create(ReportStatus.PENDING_SEND.name(), null, LocalDate.now(), LocalDate.now(), "parameters"));

        assertEquals(expectedType, exception.getMessage());

    }

    @Test
    void given_aInvalidStarDate_when_instanciate_then_shouldThrowException() {

        var expectedType = "A data de início não pode ser nula";

        var exception = assertThrows(ReportRequestInvalidException.class,
                () -> create(ReportStatus.PENDING_SEND.name(), ReportType.PDF.name(), null, LocalDate.now(),
                        "parameters"));

        assertEquals(expectedType, exception.getMessage());

    }

    @Test
    void given_strat_date_cannot_be_in_future_when_instanciate_then_shouldThrowException() {

        var expectedType = "A data de início não pode ser no futuro";

        var exception = assertThrows(ReportRequestInvalidException.class,
                () -> create(ReportStatus.PENDING_SEND.name(), ReportType.PDF.name(), LocalDate.now().plusDays(1),
                        LocalDate.now(),
                        "parameters"));

        assertEquals(expectedType, exception.getMessage());

    }

    @Test
    void given_end_date_cannot_be_in_future_when_instanciate_then_shouldThrowException() {

        var expectedType = "A data de fim não pode ser no futuro";

        var exception = assertThrows(ReportRequestInvalidException.class,
                () -> create(ReportStatus.PENDING_SEND.name(), ReportType.PDF.name(), LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        "parameters"));

        assertEquals(expectedType, exception.getMessage());

    }

    @Test
    void given_valid_reportRequest_when_updateStatus_then_shouldUpdate() {

        var expectedStatus = ReportStatus.PENDING;

        var actualReportRequest = create(ReportStatus.PENDING_SEND.name(), ReportType.PDF.name(), LocalDate.now(),
                LocalDate.now(), "parameters");

        actualReportRequest.updateStatus(expectedStatus.name());

        assertEquals(expectedStatus, actualReportRequest.getStatus());

    }

    @Test
    void given_invalid_reportRequest_when_updateStatus_then_shouldThrowException() {

        var expectedStatus = "Status do relatorio invalido";
        var actualReportRequest = create(ReportStatus.PENDING_SEND.name(), ReportType.PDF.name(), LocalDate.now(),
                LocalDate.now(), "parameters");

        var actualException = assertThrows(ReportRequestInvalidException.class,
                () -> actualReportRequest.updateStatus("invalid status"));

        assertEquals(expectedStatus, actualException.getMessage());

    }

}