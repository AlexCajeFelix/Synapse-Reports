package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.GetReportUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/report-requests")
public class ReportRequestController {

    private final ReportMapper reportMapper;
    private final CreateReportUseCase createReportUseCase;
    private final GetReportUseCase getReportUseCase;

    public ReportRequestController(ReportMapper reportMapper, CreateReportUseCase createReportUseCase,
            GetReportUseCase getReportUseCase) {
        this.createReportUseCase = createReportUseCase;
        this.reportMapper = reportMapper;
        this.getReportUseCase = getReportUseCase;

    }

    @Operation(summary = "enviar report")
    @PostMapping
    public CompletableFuture<ResponseEntity<ReportRequestResponseDto>> createReportRequest(
            @RequestBody CreateReportRequestDto requestDto) {

        var entitie = reportMapper.toEntity(requestDto);

        return createReportUseCase.execute(entitie)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {

                    System.out.println("Erro ao criar relatório: " + ex.getMessage());

                    var response = reportMapper.toResponseDto(entitie);

                    return ResponseEntity.status(202).body(response);
                });

    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportRequest> getReportRequest(@PathVariable String id) {

        var reportRequest = getReportUseCase.execute(UUID.fromString(id));

        return ResponseEntity.ok(reportRequest);

    }

}