package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.ReportRequestInvalidException;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportRequestServiceUseCaseImplTest {

        @InjectMocks
        private ReportRequestServiceUseCaseImpl report_request_service_use_case_impl;

        @Test
        @DisplayName("given_report_start_date_after_today_when_execute_then_throw_report_request_invalid_exception")
        void given_report_start_date_after_today_when_execute_then_throw_report_request_invalid_exception() {
                assertThrows(ReportRequestInvalidException.class, () -> {
                        var entity = new ReportRequest(
                                        "VENDAS",
                                        LocalDate.now().plusDays(1), // data início depois de hoje
                                        LocalDate.now(),
                                        "{}");
                        report_request_service_use_case_impl.requestNewReport(entity);
                });
        }

        @Test
        @DisplayName("given_report_end_date_after_today_when_execute_then_throw_report_request_invalid_exception")
        void given_report_end_date_after_today_when_execute_then_throw_report_request_invalid_exception() {
                assertThrows(ReportRequestInvalidException.class, () -> {
                        var entity = new ReportRequest(
                                        "VENDAS",
                                        LocalDate.now().minusDays(5),
                                        LocalDate.now().plusDays(1), // data fim depois de hoje
                                        "{}");
                        report_request_service_use_case_impl.requestNewReport(entity);
                });
        }

        @Test
        @DisplayName("given_report_type_null_when_execute_then_throw_report_request_invalid_exception")
        void given_report_type_null_when_execute_then_throw_report_request_invalid_exception() {
                assertThrows(ReportRequestInvalidException.class, () -> {
                        var entity = new ReportRequest(
                                        null,
                                        LocalDate.now().minusDays(5),
                                        LocalDate.now(),
                                        "{}");
                        report_request_service_use_case_impl.requestNewReport(entity);
                });
        }

        @Test
        @DisplayName("given_report_type_blank_when_execute_then_throw_report_request_invalid_exception")
        void given_report_type_blank_when_execute_then_throw_report_request_invalid_exception() {
                assertThrows(ReportRequestInvalidException.class, () -> {
                        var entity = new ReportRequest(
                                        "   ",
                                        LocalDate.now().minusDays(5),
                                        LocalDate.now(),
                                        "{}");
                        report_request_service_use_case_impl.requestNewReport(entity);
                });
        }

}
