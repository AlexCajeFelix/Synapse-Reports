package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportRequestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_report_should_return_pending_status_and_valid_id() {
        var dto = new CreateReportRequestDto("vendas", LocalDate.now(), LocalDate.now().plusDays(1), "value");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity("/api/report-requests", request,
                ReportRequestResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        var body = response.getBody();
        assertThat(body.reportId()).isNotNull();
        assertThat(body.status()).isEqualTo("PENDING");
        assertThat(body.message())
                .isEqualTo("Seu pedido de relatório foi recebido e está na fila para processamento.");
    }

    @Test
    void create_report_with_missing_reportType_should_return_bad_request() {
        // reportType null (missing)
        var dto = new CreateReportRequestDto(null, LocalDate.now(), LocalDate.now().plusDays(1), "value");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity("/api/report-requests", request,
                ReportRequestResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        var body = response.getBody();
        assertThat(body.status()).isEqualTo("Error");
        assertThat(body.message()).containsIgnoringCase("reportType"); // Mensagem deve falar do erro
    }

    @Test
    void create_report_with_invalid_date_range_should_return_bad_request() {

        var dto = new CreateReportRequestDto("vendas", LocalDate.now().plusDays(2), LocalDate.now().plusDays(1),
                "value");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity("/api/report-requests", request,
                ReportRequestResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        var body = response.getBody();
        assertThat(body.status()).isEqualTo("Error");
        assertThat(body.message()).containsIgnoringCase("date");
    }

    @Test
    void create_report_with_empty_parameters_should_return_bad_request() {
        // parameters empty string
        var dto = new CreateReportRequestDto("vendas", LocalDate.now(), LocalDate.now().plusDays(1), "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ReportRequestResponseDto> response = restTemplate.postForEntity("/api/report-requests", request,
                ReportRequestResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        var body = response.getBody();
        assertThat(body.status()).isEqualTo("Error");
        assertThat(body.message()).containsIgnoringCase("parameters");
    }

    @Test
    void create_report_with_invalid_json_should_return_bad_request() {
        String invalidJson = "{\"reportType\": \"vendas\", \"reportStartDate\": \"2024-13-01\"}"; // mês inválido

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/report-requests", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("JSON"); // ou outra mensagem de erro do parser JSON
    }
}
