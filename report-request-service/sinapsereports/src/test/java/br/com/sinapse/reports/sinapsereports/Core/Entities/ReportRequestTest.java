package br.com.sinapse.reports.sinapsereports.Core.Entities;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.ReportRequestInvalidException;

import static br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest.create;
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

        var aReportRequest = create(ReportStatus.PENDING_SEND, ReportType.PDF, expectedDate,
                expectedDate, "parameters");

        assertNotNull(aReportRequest.getId());
        assertEquals(expectedReportStatus, aReportRequest.getStatus());
        assertEquals(expectedReportType, aReportRequest.getReportType());
        assertEquals(expectedDate, aReportRequest.getReportStartDate());
        assertEquals(expectedDate, aReportRequest.getReportEndDate());
        assertEquals(expectedParameters, aReportRequest.getParameters());

    }

    @Test
    void given_aInvalidReportRequest_when_instanciate_then_shouldThrowException() {

        var exception = assertThrows(ReportRequestInvalidException.class,
                () -> create(null, null, null, null, null));

        assertEquals("status nao pode ser nulo", exception.getMessage());

    }

}
