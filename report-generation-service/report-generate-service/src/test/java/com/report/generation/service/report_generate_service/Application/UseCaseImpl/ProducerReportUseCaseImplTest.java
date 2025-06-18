package com.report.generation.service.report_generate_service.Application.UseCaseImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.report.generation.service.report_generate_service.Application.Enum.ReportStatus;
import com.report.generation.service.report_generate_service.Application.Enum.ReportType;
import com.report.generation.service.report_generate_service.Domain.Entities.Report;
import com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException.SendReportToKafkaException;
import com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException.SendReportToRabbitMqException;

@ExtendWith(MockitoExtension.class)
public class ProducerReportUseCaseImplTest {

    @Mock
    private ProducerReportUseCaseImpl producerReportUseCaseImpl;

    @Test
    void given_report_valid_when_send_to_kafka_then_send_to_kafka() {
        // Given
        Report report = new Report(
                UUID.randomUUID(),
                ReportType.CSV,
                ReportStatus.PENDING,
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                null,
                null);
        doNothing().when(producerReportUseCaseImpl).sendReportToKafka(report);

        assertDoesNotThrow(() -> producerReportUseCaseImpl.sendReportToKafka(report));

        verify(producerReportUseCaseImpl, times(1)).sendReportToKafka(report);
    }

    @Test
    void given_report_valid_when_send_to_rabbitmq_then_send_to_rabbitmq() {
        // Given
        Report report = new Report(
                UUID.randomUUID(),
                ReportType.CSV,
                ReportStatus.PENDING,
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                null,
                null);
        doNothing().when(producerReportUseCaseImpl).sendReportToRabbitMQ(report);

        assertDoesNotThrow(() -> producerReportUseCaseImpl.sendReportToRabbitMQ(report));

        verify(producerReportUseCaseImpl, times(1)).sendReportToRabbitMQ(report);
    }

    @Test
    void given_report_valid_when_send_to_kafka_and_rabbitmq_then_send_to_kafka_and_rabbitmq() {
        // Given
        Report report = new Report(
                UUID.randomUUID(),
                ReportType.CSV,
                ReportStatus.PENDING,
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                null,
                null);
        doNothing().when(producerReportUseCaseImpl).sendReportToKafka(report);
        doNothing().when(producerReportUseCaseImpl).sendReportToRabbitMQ(report);

        assertDoesNotThrow(() -> producerReportUseCaseImpl.sendReportToKafka(report));
        assertDoesNotThrow(() -> producerReportUseCaseImpl.sendReportToRabbitMQ(report));

        verify(producerReportUseCaseImpl, times(1)).sendReportToKafka(report);
        verify(producerReportUseCaseImpl, times(1)).sendReportToRabbitMQ(report);
    }

    @Test
    void given_report_invalid_when_send_to_kafka_then_to_throw_exception() {
        // Given
        Report report = new Report(
                UUID.randomUUID(),
                ReportType.CSV,
                ReportStatus.PENDING,
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                null,
                null);
        doThrow(new SendReportToKafkaException("Error send to kafka")).when(producerReportUseCaseImpl)
                .sendReportToKafka(report);

        assertThrows(SendReportToKafkaException.class, () -> producerReportUseCaseImpl.sendReportToKafka(report),
                "Error send to kafka");
    }

    @Test
    void given_report_invalid_when_send_to_rabbitMq_then_to_throw_exception() {
        // Given
        Report report = new Report(
                UUID.randomUUID(),
                ReportType.CSV,
                ReportStatus.PENDING,
                LocalDate.now(),
                LocalDate.now(),
                LocalDateTime.now(),
                null,
                null);
        doThrow(new SendReportToRabbitMqException("Error send to rabbitMq")).when(producerReportUseCaseImpl)
                .sendReportToKafka(report);

        assertThrows(SendReportToRabbitMqException.class, () -> producerReportUseCaseImpl.sendReportToKafka(report),
                "Error send to rabbitMq");
    }

}
