package br.com.sinapse.reports.sinapsereports.infra.report.controller;

import br.com.sinapse.reports.sinapsereports.application.report.dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.application.report.dtos.ReportRequestResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "report-request", partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093", "port=9093"
})
class CreateReportControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getUrl() {
        return "http://localhost:" + port + "/report";
    }

    @Test
    @DisplayName("Deve criar relatório com sucesso via POST /report")
    void testCreateReportSuccess() {
        CreateReportRequestDto dto = new CreateReportRequestDto(
                "PENDING_SEND",
                "PDF",
                LocalDate.now(),
                LocalDate.now(),
                "expectedParameters");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ReportRequestResponseDto> response = restTemplate
                .postForEntity(getUrl(), request, ReportRequestResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().reportType().name()).isEqualTo("PDF");
    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST se reportType for nulo")
    void testCreateReportMissingReportType() {
        CreateReportRequestDto dto = new CreateReportRequestDto(
                "PENDING_SEND",
                null,
                LocalDate.now(),
                LocalDate.now(),
                "parameters");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate
                .postForEntity(getUrl(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsIgnoringCase("Tipo do relatorio invalido");

    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST se datas forem inválidas")
    void testCreateReportInvalidDateRange() {
        CreateReportRequestDto dto = new CreateReportRequestDto(
                "PENDING_SEND",
                "PDF",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(1),
                "parameters");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate
                .postForEntity(getUrl(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsIgnoringCase("data de início não pode ser no futuro");

    }

    @Test
    @DisplayName("deve retornar 202 paremtros nao obrigatorio")
    void testCreateReportEmptyParameters() {
        CreateReportRequestDto dto = new CreateReportRequestDto(
                "PENDING_SEND",
                "PDF",
                LocalDate.now(),
                LocalDate.now(),
                "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateReportRequestDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate
                .postForEntity(getUrl(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST se JSON for inválido")
    void testCreateReportInvalidJson() {
        String invalidJson = "{\"reportType\": \"PDF\", \"reportStartDate\": \"2024-13-01\"}"; // mês inválido

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);

        ResponseEntity<String> response = restTemplate
                .postForEntity(getUrl(), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}