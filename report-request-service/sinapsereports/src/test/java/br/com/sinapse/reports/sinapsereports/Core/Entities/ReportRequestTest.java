package br.com.sinapse.reports.sinapsereports.Core.Entities;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Validators.ReportRequestValidator;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportRequestTest {

    @Test
    void create_validReportRequest_shouldPass() {
        ReportRequest report = new ReportRequest(
                "financial",
                LocalDate.now().minusDays(5),
                LocalDate.now(),
                "{\"param\": \"value\"}");

        assertEquals("financial", report.getReportType());
        assertNotNull(report.getRequestedAt());
        assertEquals(LocalDate.now().minusDays(5), report.getReportStartDate());
        assertEquals(LocalDate.now(), report.getReportEndDate());
        assertEquals("{\"param\": \"value\"}", report.getParameters());
        assertEquals(br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus.PENDING, report.getStatus());
    }

    @Test
    void create_nullReportType_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                null,
                LocalDate.now().minusDays(5),
                LocalDate.now(),
                "{}"));
        assertEquals("Report type must not be blank.", ex.getMessage());
    }

    @Test
    void create_blankReportType_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                "  ",
                LocalDate.now().minusDays(5),
                LocalDate.now(),
                "{}"));
        assertEquals("Report type must not be blank.", ex.getMessage());
    }

    @Test
    void create_nullStartDate_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                "type",
                null,
                LocalDate.now(),
                "{}"));
        assertEquals("Start and end dates must not be null.", ex.getMessage());
    }

    @Test
    void create_nullEndDate_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                "type",
                LocalDate.now().minusDays(5),
                null,
                "{}"));
        assertEquals("Start and end dates must not be null.", ex.getMessage());
    }

    @Test
    void create_startDateAfterEndDate_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                "type",
                LocalDate.now(),
                LocalDate.now().minusDays(1),
                "{}"));
        assertEquals("Start date must be before or equal to end date.", ex.getMessage());
    }

    @Test
    void create_startDateInFuture_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                "type",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                "{}"));
        assertEquals("Start date must not be in the future.", ex.getMessage());
    }

    @Test
    void create_endDateInFuture_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ReportRequest(
                "type",
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(1),
                "{}"));
        assertEquals("End date must not be in the future.", ex.getMessage());
    }

    @Test
    void create_nullReportRequest_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ReportRequestValidator.validate(null));
        assertEquals("ReportRequest must not be null.", ex.getMessage());
    }
}
