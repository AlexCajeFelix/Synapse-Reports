package br.com.sinapse.reports.sinapsereports.Core.Entities;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.ReportRequestInvalidException;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.Notification;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorsRules.ReportRequestValidator;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportRequestTest {

    @Test
    void create_valid_report_request_should_pass() {
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
    void create_null_report_type_should_throw() {
        ReportRequestInvalidException ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                null,
                LocalDate.now().minusDays(5),
                LocalDate.now(),
                "{}"));
        assertEquals("Report type must not be blank.", ex.getMessage());
    }

    @Test
    void create_blank_report_type_should_throw() {
        var ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                "  ",
                LocalDate.now().minusDays(5),
                LocalDate.now(),
                "{}"));
        assertEquals("Report type must not be blank.", ex.getMessage());
    }

    @Test
    void create_null_start_date_should_throw() {
        var ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                "type",
                null,
                LocalDate.now(),
                "{}"));
        assertEquals("Start and end dates must not be null.", ex.getMessage());
    }

    @Test
    void create_null_end_date_should_throw() {
        var ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                "type",
                LocalDate.now().minusDays(5),
                null,
                "{}"));
        assertEquals("Start and end dates must not be null.", ex.getMessage());
    }

    @Test
    void create_start_date_after_end_date_should_throw() {
        var ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                "type",
                LocalDate.now(),
                LocalDate.now().minusDays(1),
                "{}"));
        assertEquals("Start date must be before or equal to end date.", ex.getMessage());
    }

    @Test
    void create_start_date_in_future_should_throw() {
        var ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                "type",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                "{}"));
        assertTrue(ex.getMessage().contains("Start date must not be in the future."));
    }

    @Test
    void create_end_date_in_future_should_throw() {
        ReportRequestInvalidException ex = assertThrows(ReportRequestInvalidException.class, () -> new ReportRequest(
                "type",
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(1),
                "{}"));
        assertEquals("End date must not be in the future.", ex.getMessage());
    }

    @Test
    void create_null_report_request_should_report_error() {
        ReportRequestValidator validator = new ReportRequestValidator();
        Notification<ReportRequest> notification = validator.validate(null);

        assertTrue(notification.hasErrors());
        assertTrue(notification.getErrors().stream()
                .anyMatch(e -> e.getMessage().equals("ReportRequest must not be null.")));
    }
}
