package br.com.sinapse.reports.sinapsereports.application.report.usecaseimpl;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import br.com.sinapse.reports.sinapsereports.application.report.dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.application.report.usecase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.gateway.ReportCommandGateway;

import static br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest.create;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateReportUseCaseImplTest {

        @Mock
        private ReportCommandGateway reportCommandGateway;

        @Mock
        private PublishToKafkaUseCase publishToKafkaUseCase;

        @InjectMocks
        private CreateReportUseCaseImpl createReportUseCaseImpl;

        @Test
        void given_validReport_when_create_then_return_report_and_publishToKafka() {
                var dto = new CreateReportRequestDto(
                                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

                var createdReport = create(
                                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

                when(reportCommandGateway.create(any())).thenReturn(createdReport);
                doNothing().when(publishToKafkaUseCase).execute(any());

                var report = createReportUseCaseImpl.execute(dto);

                assertNotNull(report);
                assertNotNull(report.getId());

                await().atMost(5, TimeUnit.SECONDS)
                                .untilAsserted(() -> verify(publishToKafkaUseCase).execute(any()));
                verify(reportCommandGateway, times(1)).create(any(ReportRequest.class));
                verify(publishToKafkaUseCase, times(1)).execute(any(ReportRequest.class));
        }

        @Test
        void given_reportGatewayFails_when_create_then_publishNotCalled() {
                var dto = new CreateReportRequestDto(
                                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

                when(reportCommandGateway.create(any()))
                                .thenThrow(new IllegalArgumentException("Error creating report"));

                createReportUseCaseImpl.execute(dto);

                await().atMost(5, TimeUnit.SECONDS)
                                .untilAsserted(() -> verify(publishToKafkaUseCase, never()).execute(any()));

                verify(publishToKafkaUseCase, never()).execute(any());

        }

        @Test
        void given_nullInput_when_create_then_throwException() {
                var exception = assertThrows(NullPointerException.class,
                                () -> createReportUseCaseImpl.execute(null));

                assertEquals("Objeto não pode ser nulo", exception.getMessage());
        }

        @Test
        void given_kafkaFails_when_publish_then_should_logErrorAndNotCrash() {
                var dto = new CreateReportRequestDto(
                                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

                var createdReport = create(
                                "PENDING_SEND", "PDF", LocalDate.now(), LocalDate.now(), "parameters");

                when(reportCommandGateway.create(any())).thenReturn(createdReport);
                doThrow(new RuntimeException("Kafka error")).when(publishToKafkaUseCase).execute(any());

                createReportUseCaseImpl.execute(dto);

                await().atMost(5, TimeUnit.SECONDS)
                                .untilAsserted(() -> verify(publishToKafkaUseCase).execute(any()));
        }
}
