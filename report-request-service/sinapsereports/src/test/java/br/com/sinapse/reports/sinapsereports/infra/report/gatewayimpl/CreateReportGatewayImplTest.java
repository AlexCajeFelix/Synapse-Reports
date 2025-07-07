package br.com.sinapse.reports.sinapsereports.infra.report.gatewayimpl;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.infra.report.mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.entitiesjpa.ReportRequestEntity;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository.ReportRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateReportGatewayImplTest {

    private ReportRepositoryJpa reportRepositoryJpa;
    private CreateReportGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        reportRepositoryJpa = mock(ReportRepositoryJpa.class);
        gateway = new CreateReportGatewayImpl(reportRepositoryJpa);
    }

    @Test
    @DisplayName("Deve criar relatório com sucesso")
    void testCreateSuccess() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entityJpa = mock(ReportRequestEntity.class);

        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entityJpa);
            when(reportRepositoryJpa.save(entityJpa)).thenReturn(entityJpa);

            ReportRequest result = gateway.create(reportRequest);

            assertEquals(reportRequest, result);
            verify(reportRepositoryJpa).save(entityJpa);
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar relatório se salvar retornar null")
    void testCreateThrowsWhenSaveReturnsNull() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entityJpa = mock(ReportRequestEntity.class);

        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entityJpa);
            when(reportRepositoryJpa.save(entityJpa)).thenReturn(null);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> gateway.create(reportRequest));
            assertEquals("Erro ao salvar report", ex.getMessage());
        }
    }

    @Test
    @DisplayName("Deve buscar relatório por ID com sucesso")
    void testFindByIdSuccess() {
        UUID id = UUID.randomUUID();
        ReportRequestEntity entityJpa = mock(ReportRequestEntity.class);
        ReportRequest reportRequest = mock(ReportRequest.class);

        when(reportRepositoryJpa.findById(id)).thenReturn(Optional.of(entityJpa));
        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toDomain(entityJpa)).thenReturn(reportRequest);

            ReportRequest result = gateway.findByID(id);

            assertEquals(reportRequest, result);
        }
    }

    @Test
    @DisplayName("Deve retornar null ao buscar relatório por ID inexistente")
    void testFindByIdReturnsNullWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(reportRepositoryJpa.findById(id)).thenReturn(Optional.empty());

        ReportRequest result = gateway.findByID(id);

        assertNull(result);
    }

    @Test
    @DisplayName("Deve atualizar relatório com sucesso")
    void testUpdateSuccess() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entityJpa = mock(ReportRequestEntity.class);

        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entityJpa);
            when(reportRepositoryJpa.save(entityJpa)).thenReturn(entityJpa);

            assertDoesNotThrow(() -> gateway.update(reportRequest, ReportStatus.PENDING));
            verify(reportRepositoryJpa).save(entityJpa);
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar relatório se salvar retornar null")
    void testUpdateThrowsWhenSaveReturnsNull() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entityJpa = mock(ReportRequestEntity.class);

        try (MockedStatic<ReportMapper> mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entityJpa);
            when(reportRepositoryJpa.save(entityJpa)).thenReturn(null);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> gateway.update(reportRequest, ReportStatus.PENDING));
            assertEquals("Erro ao salvar report", ex.getMessage());
        }
    }
}