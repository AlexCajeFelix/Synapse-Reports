package br.com.sinapse.reports.sinapsereports.infra.report.controller;

import br.com.sinapse.reports.sinapsereports.application.report.usecase.GetReportUseCase;
import br.com.sinapse.reports.sinapsereports.domain.report.ReportRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class GetReportController {

    private final GetReportUseCase getReportUseCase;

    public GetReportController(GetReportUseCase getReportUseCase) {
        this.getReportUseCase = getReportUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportRequest> getReportById(@PathVariable UUID id) {
        var report = getReportUseCase.execute(id);

        if (report == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(report);
    }
}
