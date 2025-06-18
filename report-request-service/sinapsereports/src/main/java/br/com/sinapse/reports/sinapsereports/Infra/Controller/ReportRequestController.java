package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.CreateReportUseCase;

@RestController
@RequestMapping("/api/report-requests")
public class ReportRequestController {

    private final ReportMapper reportMapper;
    private final CreateReportUseCase createReportUseCase;

    public ReportRequestController(ReportMapper reportMapper, CreateReportUseCase createReportUseCase) {
        this.createReportUseCase = createReportUseCase;
        this.reportMapper = reportMapper;

    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ReportRequestResponseDto>> createReportRequest(
            @RequestBody CreateReportRequestDto requestDto) {
        var entitie = reportMapper.toEntity(requestDto);

        return createReportUseCase.execute(entitie)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    // Log ou print da exceção
                    System.out.println("Erro ao criar relatório: " + ex.getMessage());

                    var response = reportMapper.toResponseDto(entitie);

                    return ResponseEntity.status(202).body(response);
                });

    }

}