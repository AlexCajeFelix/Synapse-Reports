package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.CreateReportUseCase;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.PublishToKafkaUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Gateway.ReportCommandGateway;
import lombok.var;

@Service
public class CreateReportUseCaseImpl extends CreateReportUseCase {

    public CreateReportUseCaseImpl(ReportCommandGateway reportCommandGateway,
            PublishToKafkaUseCase publishToKafkaUseCase) {
        super(reportCommandGateway, publishToKafkaUseCase);
    }

    @Override
    public CompletableFuture<ReportRequest> execute(CreateReportRequestDto aReportDto) {
        Objects.requireNonNull(aReportDto, "Objeto não pode ser nulo");

        return CompletableFuture.supplyAsync(() -> {
            var aReport = ReportRequest.create(
                    aReportDto.status(),
                    aReportDto.reportType(),
                    aReportDto.reportStartDate(),
                    aReportDto.reportEndDate(),
                    aReportDto.parameters());
            return aReport;
        }).thenApply(aReport -> {
            var savedReport = reportCommandGateway.create(aReport);
            if (savedReport == null) {
                throw new IllegalStateException("Falha ao salvar relatório: retorno nulo do gateway");
            }

            publishToKafkaUseCase.execute(savedReport);

            return savedReport;
        });
    }
}
