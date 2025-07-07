package br.com.sinapse.reports.sinapsereports.infra.report.fallback;

import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.infra.report.mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.entitiesjpa.ReportRequestEntity;
import br.com.sinapse.reports.sinapsereports.infra.report.persistence.repository.ReportRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FallbackGatewayImplTest {

    private ReportRepositoryJpa reportRepositoryJpa;
    private FallbackGatewayImpl fallbackGateway;

    @BeforeEach
    void setUp() {
        reportRepositoryJpa = mock(ReportRepositoryJpa.class);
        fallbackGateway = new FallbackGatewayImpl(reportRepositoryJpa);
    }

    @Test
    @DisplayName("Deve atualizar status para FAILED e salvar no repositório")
    void testPublishReportUpdatesStatusAndSaves() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entity = mock(ReportRequestEntity.class);

        // Mock o mapeamento estático
        try (var mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entity);

            fallbackGateway.publishReport(reportRequest);

            verify(reportRequest).updateStatus(ReportStatus.FAILED.name());
            verify(reportRepositoryJpa).save(entity);
        }
    }

    @Test
    @DisplayName("Deve propagar exceção se o mapeamento falhar")
    void testPublishReportThrowsIfMappingFails() {
        ReportRequest reportRequest = mock(ReportRequest.class);

        try (var mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest))
                    .thenThrow(new RuntimeException("Erro no mapeamento"));

            assertThrows(RuntimeException.class, () -> fallbackGateway.publishReport(reportRequest));
        }
    }

    @Test
    @DisplayName("Deve propagar exceção se o save do repositório falhar")
    void testPublishReportThrowsIfRepositoryFails() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entity = mock(ReportRequestEntity.class);

        try (var mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entity);
            doThrow(new RuntimeException("Erro ao salvar")).when(reportRepositoryJpa).save(entity);

            assertThrows(RuntimeException.class, () -> fallbackGateway.publishReport(reportRequest));
        }
    }

    @Test
    @DisplayName("Deve chamar updateStatus mesmo se já estiver FAILED")
    void testPublishReportUpdateStatusAlwaysCalled() {
        ReportRequest reportRequest = mock(ReportRequest.class);
        ReportRequestEntity entity = mock(ReportRequestEntity.class);

        try (var mockedMapper = mockStatic(ReportMapper.class)) {
            mockedMapper.when(() -> ReportMapper.toEntity(reportRequest)).thenReturn(entity);

            fallbackGateway.publishReport(reportRequest);

            verify(reportRequest, atLeastOnce()).updateStatus(ReportStatus.FAILED.name());
        }
    }
}