package br.com.sinapse.reports.sinapsereports.Infra.Controller;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/reports")
public class CreateReportController {

    private final CreateReportUseCase createReportUseCase;

    public CreateReportController(CreateReportUseCase createReportUseCase) {
        this.createReportUseCase = createReportUseCase;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ReportRequest>> createReport(
            @Valid @RequestBody CreateReportRequestDto requestDto) {

        return createReportUseCase.execute(requestDto)
                .thenApply(ResponseEntity::ok);
    }

}
