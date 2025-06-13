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
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ReportRequestServiceUseCase;

@RestController
@RequestMapping("/api/report-requests")
public class ReportRequestController {

    private final ReportRequestServiceUseCase reportRequestService;
    private final ReportMapper reportMapper;

    public ReportRequestController(ReportRequestServiceUseCase reportRequestService, ReportMapper reportMapper) {
        this.reportRequestService = reportRequestService;
        this.reportMapper = reportMapper;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ReportRequestResponseDto>> createReportRequest(
            @RequestBody CreateReportRequestDto requestDto) {

        return reportRequestService.requestNewReport(reportMapper.toEntity(requestDto))
                .thenApply(ResponseEntity::ok);
    }

}