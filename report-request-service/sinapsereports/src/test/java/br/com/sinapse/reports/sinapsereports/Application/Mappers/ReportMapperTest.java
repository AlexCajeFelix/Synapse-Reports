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
    private ReportMapper report_mapper;

    @Test
    @DisplayName("given valid dto when to_entity then return correct entity")
    void given_valid_dto_when_to_entity_then_return_correct_entity() {

        var create_report_request_dto = new CreateReportRequestDto(
                "SALES_BY_REGION",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31),
                "Brazil");

        ReportRequest report_request_entity = report_mapper.toEntity(create_report_request_dto);

        assertNotNull(report_request_entity);
        assertEquals("SALES_BY_REGION", report_request_entity.getReportType());
        assertEquals(LocalDate.of(2025, 1, 1), report_request_entity.getReportStartDate());
        assertEquals(LocalDate.of(2025, 1, 31), report_request_entity.getReportEndDate());
        assertEquals("Brazil", report_request_entity.getParameters());
        assertEquals(ReportStatus.PENDING, report_request_entity.getStatus());
    }

    @Test
    @DisplayName("given valid entity when to_response_dto then return correct dto")
    void given_valid_entity_when_to_response_dto_then_return_correct_dto() {

        var report_request_entity = new ReportRequest();
        report_request_entity.setId(UUID.randomUUID());
        report_request_entity.setStatus(ReportStatus.PENDING);

        ReportRequestResponseDto response_dto = report_mapper.toResponseDto(report_request_entity);

        assertNotNull(response_dto);
        assertEquals(report_request_entity.getId(), response_dto.reportId());
        assertEquals(ReportStatus.PENDING.toString(), response_dto.status());
        assertNotNull(response_dto.message());
    }

    @Test
    @DisplayName("given valid entity when to_event_dto then return correct event")
    void given_valid_entity_when_to_event_dto_then_return_correct_event() {
        var report_request_entity = new ReportRequest();
        report_request_entity.setId(UUID.randomUUID());
        report_request_entity.setReportStartDate(LocalDate.of(2025, 6, 1));
        report_request_entity.setReportEndDate(LocalDate.of(2025, 6, 30));
        report_request_entity.setParameters("Brazil");

        ReportRequestedEvent event_dto = report_mapper.toEventDto(report_request_entity);

        assertNotNull(event_dto);
        assertEquals(report_request_entity.getId(), event_dto.reportId());
        assertEquals(LocalDate.of(2025, 6, 1), event_dto.reportStartDate());
        assertEquals(LocalDate.of(2025, 6, 30), event_dto.reportEndDate());
        assertEquals("Brazil", event_dto.parameters());
    }

    @Test
    @DisplayName("given null dto when to_entity then return null")
    void given_null_dto_when_to_entity_then_return_null() {
        assertNull(report_mapper.toEntity(null));
    }

    @Test
    @DisplayName("given null entity when to_response_dto then return null")
    void given_null_entity_when_to_response_dto_then_return_null() {
        assertNull(report_mapper.toResponseDto(null));
    }

    @Test
    @DisplayName("given null entity when to_event_dto then return null")
    void given_null_entity_when_to_event_dto_then_return_null() {
        assertNull(report_mapper.toEventDto(null));
    }
}
