package br.com.sinapse.reports.sinapsereports.Application.Mappers;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestedEvent;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportMapperTest {

    @InjectMocks
    private ReportMapper reportMapper;

    @Test
    @DisplayName("given valid DTO, when toEntity is called, then should return correctly mapped entity")
    void given_valid_dto_when_to_entity_then_return_correct_entity() {

        var createReportRequestDto = new CreateReportRequestDto(
                "SALES_BY_REGION",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31),
                "Brazil");

        ReportRequest reportRequestEntity = reportMapper.toEntity(createReportRequestDto);

        assertNotNull(reportRequestEntity);
        assertEquals("SALES_BY_REGION", reportRequestEntity.getReportType());
        assertEquals(LocalDate.of(2025, 1, 1), reportRequestEntity.getReportStartDate());
        assertEquals(LocalDate.of(2025, 1, 31), reportRequestEntity.getReportEndDate());
        assertEquals("Brazil", reportRequestEntity.getParameters());
        assertEquals(ReportStatus.PENDING, reportRequestEntity.getStatus());
    }

    @Test
    @DisplayName("given valid entity, when toResponseDto is called, then should return correctly mapped DTO")
    void given_valid_entity_when_to_response_dto_then_return_correct_dto() {
        // Given
        var reportRequestEntity = new ReportRequest();
        reportRequestEntity.setId(UUID.randomUUID());
        reportRequestEntity.setStatus(ReportStatus.PENDING);

        // When
        ReportRequestResponseDto responseDto = reportMapper.toResponseDto(reportRequestEntity);

        // Then
        assertNotNull(responseDto);
        assertEquals(reportRequestEntity.getId(), responseDto.reportId());
        assertEquals(ReportStatus.PENDING.toString(), responseDto.status());
        assertNotNull(responseDto.message());
    }

    @Test
    @DisplayName("given valid entity, when toEventDto is called, then should return correctly mapped event")
    void given_valid_entity_when_to_event_dto_then_return_correct_event() {
        // Given
        var reportRequestEntity = new ReportRequest();
        reportRequestEntity.setId(UUID.randomUUID());
        reportRequestEntity.setReportStartDate(LocalDate.of(2025, 6, 1));
        reportRequestEntity.setReportEndDate(LocalDate.of(2025, 6, 30));
        reportRequestEntity.setParameters("Brazil");

        // When
        ReportRequestedEvent eventDto = reportMapper.toEventDto(reportRequestEntity);

        // Then
        assertNotNull(eventDto);
        assertEquals(reportRequestEntity.getId(), eventDto.reportId());
        assertEquals(LocalDate.of(2025, 6, 1), eventDto.reportStartDate());
        assertEquals(LocalDate.of(2025, 6, 30), eventDto.reportEndDate());
        assertEquals("Brazil", eventDto.parameters());
    }

    @Test
    @DisplayName("given null DTO, when toEntity is called, then should return null")
    void given_null_dto_when_to_entity_then_return_null() {
        assertNull(reportMapper.toEntity(null));
    }

    @Test
    @DisplayName("given null entity, when toResponseDto is called, then should return null")
    void given_null_entity_when_to_response_dto_then_return_null() {
        assertNull(reportMapper.toResponseDto(null));
    }

    @Test
    @DisplayName("given null entity, when toEventDto is called, then should return null")
    void given_null_entity_when_to_event_dto_then_return_null() {
        assertNull(reportMapper.toEventDto(null));
    }
}