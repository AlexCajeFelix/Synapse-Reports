package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReportRequestControllerTest {

    @SuppressWarnings("resource")
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"))
            .withReuse(true);

    static {
        kafka.start();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportRepository reportRepository;

    @MockBean
    private org.springframework.cloud.stream.function.StreamBridge streamBridge;

    @BeforeEach
    void setup() {
        reportRepository.deleteAll();
    }

    @AfterAll
    void teardown() {
        kafka.stop();
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    @DisplayName("create_report_when_kafka_is_up_should_return_pending")
    void create_report_when_kafka_is_up_should_return_pending() {
        when(streamBridge.send(anyString(), any())).thenReturn(true);

        var dto = new CreateReportRequestDto(ReportType.PDF, LocalDate.now().minusDays(2), LocalDate.now().minusDays(1),
                "value");
        var request = new HttpEntity<>(dto, jsonHeaders());
        var url = "http://localhost:" + port + "/api/report-requests";

        var response = restTemplate.postForEntity(url, request, ReportRequestResponseDto.class);
        var body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.reportId()).isNotNull();
        assertThat(body.status()).isEqualTo(ReportStatus.PENDING.toString());
        assertThat(body.message()).isEqualTo("Seu pedido de relatório foi recebido e está na fila para processamento.");

        var savedRequest = reportRepository.findById(body.reportId()).orElseThrow();
        assertThat(savedRequest.getStatus()).isEqualTo(ReportStatus.PENDING);
    }

    @Test
    @DisplayName("create_report_when_kafka_is_down_should_return_pendente_envio_and_fallback")
    void create_report_when_kafka_is_down_should_return_pendente_envio_and_fallback() {
        doThrow(new RuntimeException("Simulando falha Kafka")).when(streamBridge).send(anyString(), any());

        var dto = new CreateReportRequestDto(ReportType.PDF, LocalDate.now().minusDays(5), LocalDate.now(),
                "another_value");
        var request = new HttpEntity<>(dto, jsonHeaders());
        var url = "http://localhost:" + port + "/api/report-requests";

        var response = restTemplate.postForEntity(url, request, ReportRequestResponseDto.class);
        var body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.reportId()).isNotNull();
        assertThat(body.status()).isEqualTo(ReportStatus.PENDENTE_ENVIO.toString());
        assertThat(body.message()).isEqualTo("Seu pedido de relatório foi recebido e está na fila para processamento.");

        var savedRequest = reportRepository.findById(body.reportId()).orElseThrow();
        assertThat(savedRequest.getStatus()).isEqualTo(ReportStatus.PENDENTE_ENVIO);
    }

    @Test
    @DisplayName("create_report_with_invalid_data_should_return_bad_request")
    void create_report_with_invalid_data_should_return_bad_request() {
        var dto = new CreateReportRequestDto(null, LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), "value");
        var request = new HttpEntity<>(dto, jsonHeaders());
        var url = "http://localhost:" + port + "/api/report-requests";

        var response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders jsonHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}