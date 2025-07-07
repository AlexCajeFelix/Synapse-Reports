package br.com.sinapse.reports.sinapsereports.infra.report.controller;

import br.com.sinapse.reports.sinapsereports.application.report.dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.application.report.usecase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class CreateReportController {

    private final CreateReportUseCase createReportUseCase;

    public CreateReportController(CreateReportUseCase createReportUseCase) {
        this.createReportUseCase = createReportUseCase;
    }

    @PostMapping
    public ResponseEntity<ReportRequest> createReport(
            @RequestBody CreateReportRequestDto requestDto) {
        var request = createReportUseCase.execute(requestDto);

        return ResponseEntity.accepted().body(request);

    }

}
