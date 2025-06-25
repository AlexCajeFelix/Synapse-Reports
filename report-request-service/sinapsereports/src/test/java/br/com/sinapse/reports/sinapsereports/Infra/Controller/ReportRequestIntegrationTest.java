package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.awaitility.Awaitility.await;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;

import static br.com.sinapse.reports.sinapsereports.Factory.CreateRequestDtoFactory.createValidCreateReportRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EmbeddedKafka(topics = "report-request", partitions = 1, brokerProperties = {
                "listeners=PLAINTEXT://localhost:9093", "port=9093" })
public class ReportRequestIntegrationTest {

        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        public void testCreateReportRequest_success() {
                var requestDto = createValidCreateReportRequestDto();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<CreateReportRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

                ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity(
                                "/api/report-requests",
                                requestEntity,
                                ReportRequestResponseDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals(ReportStatus.PENDING, response.getBody().status());
        }

        @Test
        public void given_valid_report_when_create_report_request_then_status_should_eventually_be_pending_send() {

                var requestDto = createValidCreateReportRequestDto();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<CreateReportRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

                ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity(
                                "/api/report-requests",
                                requestEntity,
                                ReportRequestResponseDto.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                UUID reportId = response.getBody().reportId();
                assertNotNull(reportId);

                await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
                        ResponseEntity<ReportRequest> getResponse = restTemplate.getForEntity(
                                        "/api/report-requests/" + reportId,
                                        ReportRequest.class);

                        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
                        assertNotNull(getResponse.getBody());
                        assertEquals(reportId, getResponse.getBody().getId());
                        assertEquals(ReportStatus.PENDING, getResponse.getBody().getStatus());
                });

        }

}