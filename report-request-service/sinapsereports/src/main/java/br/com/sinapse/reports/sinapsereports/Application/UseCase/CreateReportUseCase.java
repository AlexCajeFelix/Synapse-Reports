package br.com.sinapse.reports.sinapsereports.Application.UseCase;

import java.util.concurrent.CompletableFuture;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract.InputOutputUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;

public abstract class CreateReportUseCase
        extends InputOutputUseCase<ReportRequest, CompletableFuture<ReportRequestResponseDto>> {

}
