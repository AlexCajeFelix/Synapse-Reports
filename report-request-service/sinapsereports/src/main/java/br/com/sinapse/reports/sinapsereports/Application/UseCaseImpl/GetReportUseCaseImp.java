package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Application.UseCase.GetReportUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.GetReportException;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;

@Service
public class GetReportUseCaseImp extends GetReportUseCase {

    private final ReportRepository reportRepository;

    public GetReportUseCaseImp(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public ReportRequest execute(UUID input) {

        return reportRepository.findById(input).orElseThrow(() -> new GetReportException("Report not found"));
    }

}
