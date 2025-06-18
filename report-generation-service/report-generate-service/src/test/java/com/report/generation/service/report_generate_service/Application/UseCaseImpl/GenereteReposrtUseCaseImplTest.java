package com.report.generation.service.report_generate_service.Application.UseCaseImpl;

import com.report.generation.service.report_generate_service.Application.Enum.ReportStatus;
import com.report.generation.service.report_generate_service.Application.Enum.ReportType;
import com.report.generation.service.report_generate_service.Domain.Entities.Report;
import com.report.generation.service.report_generate_service.Domain.Entities.SalesReportEntity;
import com.report.generation.service.report_generate_service.Infra.Repository.ReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils; // Keep this for @Value fields

import java.io.IOException;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GenereteReposrtUseCaseImplTest {

    @Mock
    private ReportRepository mockReportRepository;

    @Mock
    private ProducerReportUseCaseImpl mockProducerReportUseCaseImpl;

    @InjectMocks
    @Spy
    private GenereteReposrtUseCaseImpl genereteReposrtUseCaseImpl;

    @TempDir
    Path tempDownloadsDir; // JUnit 5 provides a temporary directory for file tests

    private Report testReport;
    private List<SalesReportEntity> mockSalesReports;
    private AutoCloseable openMocks;

    @BeforeEach
    void setup_test_environment() throws Exception {

        openMocks = MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(genereteReposrtUseCaseImpl, "storagePath", tempDownloadsDir.toString());
        ReflectionTestUtils.setField(genereteReposrtUseCaseImpl, "downloadServiceUrl",
                "http://localhost:8084/api/download");
        ReflectionTestUtils.setField(genereteReposrtUseCaseImpl, "reportNameFile", "relatorio_");

        testReport = new Report(
                UUID.randomUUID(),
                ReportType.PDF,
                ReportStatus.PENDING,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 31),
                LocalDateTime.now(),
                "param1=value1",
                null);

        mockSalesReports = Arrays.asList(
                new SalesReportEntity("Product A", LocalDate.of(2023, 1, 15), "Details A"),
                new SalesReportEntity("Product B", LocalDate.of(2023, 1, 20), "Details B"));

        when(mockReportRepository.findByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockSalesReports);

        doNothing().when(mockProducerReportUseCaseImpl).sendReportToKafka(any(Report.class));
        doNothing().when(mockProducerReportUseCaseImpl).sendReportToRabbitMQ(any(Report.class));
    }

    @AfterEach
    void tear_down() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    @DisplayName("should_throw_runtime_exception_when_downloads_folder_creation_fails")
    void should_throw_runtime_exception_when_downloads_folder_creation_fails() throws IOException {
        // Given
        // Simulate mkdirs() failing by having `getDownloadsFolder` throw the exception
        doThrow(new RuntimeException("Simulated folder creation failure"))
                .when(genereteReposrtUseCaseImpl)
                .getDownloadsFolder(any(Report.class));

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> genereteReposrtUseCaseImpl.generateReport(testReport));

        assertTrue(exception.getMessage().contains("Simulated folder creation failure"),
                "Exception message should indicate folder creation failure.");

        verifyNoInteractions(mockReportRepository);
        verifyNoInteractions(mockProducerReportUseCaseImpl);
    }

}