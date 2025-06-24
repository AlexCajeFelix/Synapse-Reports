package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static br.com.sinapse.reports.sinapsereports.Factory.CreateRequestDtoFactory.createValidCreateReportRequestDto;

import java.time.Duration;
import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.cloud.stream.function.StreamBridge;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReportRequestKafkaFailureTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StreamBridge streamBridge;

    @Test
    void given_kafka_down_when_create_report_request_then_status_should_eventually_be_failed() {

        when(streamBridge.send(anyString(), any())).thenReturn(false);

        var requestDto = createValidCreateReportRequestDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReportRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity(
                "/api/report-requests",
                requestEntity,
                ReportRequestResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UUID reportId = response.getBody().reportId();
        assertNotNull(reportId);

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            ResponseEntity<ReportRequest> getResponse = restTemplate.getForEntity(
                    "/api/report-requests/" + reportId,
                    ReportRequest.class);

            assertEquals(HttpStatus.OK, getResponse.getStatusCode());
            assertNotNull(getResponse.getBody());
            assertEquals(reportId, getResponse.getBody().getId());
            assertEquals(ReportStatus.FAILED, getResponse.getBody().getStatus(),
                    "O status do relatório deveria ser FAILED após o fallback.");
        });
    }
}
