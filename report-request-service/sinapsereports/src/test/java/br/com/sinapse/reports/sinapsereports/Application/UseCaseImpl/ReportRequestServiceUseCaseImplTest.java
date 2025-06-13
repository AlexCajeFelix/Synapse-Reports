package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportRequestServiceUseCaseImplTest {

        @InjectMocks
        private ReportRequestServiceUseCaseImpl reportRequestServiceUseCaseImpl;

        @Test
        @DisplayName("given_reportStartDate_after_today_when_execute_then_throw_IllegalArgumentException")
        void given_reportStartDate_after_today_when_execute_then_throw_IllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> {
                        var entity = new ReportRequest(
                                        "VENDAS",
                                        LocalDate.now().plusDays(1), // data início depois de hoje
                                        LocalDate.now(),
                                        "{}");
                        reportRequestServiceUseCaseImpl.requestNewReport(entity);
                });
        }

        @Test
        @DisplayName("given_reportEndDate_after_today_when_execute_then_throw_IllegalArgumentException")
        void given_reportEndDate_after_today_when_execute_then_throw_IllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> {
                        var entity = new ReportRequest(
                                        "VENDAS",
                                        LocalDate.now().minusDays(5),
                                        LocalDate.now().plusDays(1), // data fim depois de hoje
                                        "{}");
                        reportRequestServiceUseCaseImpl.requestNewReport(entity);
                });
        }

        @Test
        @DisplayName("given_reportType_null_when_execute_then_throw_IllegalArgumentException")
        void given_reportType_null_when_execute_then_throw_IllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> {
                        var entity = new ReportRequest(
                                        null,
                                        LocalDate.now().minusDays(5),
                                        LocalDate.now(),
                                        "{}");
                        reportRequestServiceUseCaseImpl.requestNewReport(entity);
                });
        }

        @Test
        @DisplayName("given_reportType_blank_when_execute_then_throw_IllegalArgumentException")
        void given_reportType_blank_when_execute_then_throw_IllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> {
                        var entity = new ReportRequest(
                                        "   ",
                                        LocalDate.now().minusDays(5),
                                        LocalDate.now(),
                                        "{}");
                        reportRequestServiceUseCaseImpl.requestNewReport(entity);
                });
        }

}
