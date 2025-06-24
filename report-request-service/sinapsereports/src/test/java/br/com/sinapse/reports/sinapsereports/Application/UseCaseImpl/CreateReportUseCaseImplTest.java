package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import static br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Factory.ReportRequestFactory.createValidReportRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;

@ExtendWith(MockitoExtension.class)
public class CreateReportUseCaseImplTest {

    @InjectMocks
    private CreateReportUseCaseImpl createReportUseCaseImpl;

    @Mock
    private PublishToKafkaUseCaseUseCaseImp publishToKafkaUseCaseUseCaseImp;

    @Mock
    private ManageStatusUseCaseImpl manageStatusUseCase;

    @Mock
    private ReportMapper reportMapper;

    @Test
    public void given_null_report_request_when_execute_then_throw_message_publishing_exception() {
        doThrow(new MessagePublishingException("Solicitação nula. Nenhuma publicação realizada."))
                .when(manageStatusUseCase).execute(isNull(), any());

        assertThrows(MessagePublishingException.class, () -> createReportUseCaseImpl.execute(null).join());
    }

    @Test
    void given_valid_report_when_execute_then_return_report_request_response_dto() {
        // given
        var report = createValidReportRequest();
        var expectedResponse = new ReportRequestResponseDto(
                report.getId(),
                report.getReportType(),
                report.getStatus(),
                "deu tudo certo");

        when(reportMapper.toResponseDto(report)).thenReturn(expectedResponse);
        doNothing().when(publishToKafkaUseCaseUseCaseImp).execute(report);
        doNothing().when(manageStatusUseCase).execute(any(), any());

        var future = createReportUseCaseImpl.execute(report);

        assertDoesNotThrow(() -> {
            var result = future.join();
            assertEquals(expectedResponse.reportId(), result.reportId());
            assertEquals(expectedResponse.reportType(), result.reportType());
            assertEquals(expectedResponse.status(), result.status());
            assertEquals(expectedResponse.message(), result.message());
        });
    }

    @Test
    void given_valid_report_when_execute_send_to_kafka_not_envied_then_throw_message_publishing_exception() {
        var report = createValidReportRequest();

        doThrow(new MessagePublishingException("Falha ao enviar para o Kafka"))
                .when(publishToKafkaUseCaseUseCaseImp).execute(report);

        var future = createReportUseCaseImpl.execute(report);

        var exception = assertThrows(CompletionException.class, () -> future.join());
        assertTrue(exception.getCause() instanceof MessagePublishingException);
    }

    @Test
    void given_valid_report_when_manage_status_fails_then_throw_message_publishing_exception() {
        var report = createValidReportRequest();

        doThrow(new MessagePublishingException("metodo manageStatus falhou"))
                .when(manageStatusUseCase).execute(any(), any());

        var exception = assertThrows(MessagePublishingException.class,
                () -> createReportUseCaseImpl.execute(report));

        assertEquals("metodo manageStatus falhou", exception.getMessage());
    }

}
