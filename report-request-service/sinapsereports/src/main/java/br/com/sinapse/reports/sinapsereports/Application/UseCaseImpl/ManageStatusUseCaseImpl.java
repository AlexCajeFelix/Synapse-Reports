package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ManageStatusUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.MessagePublishingException;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;

@Service
public class ManageStatusUseCaseImpl extends ManageStatusUseCase {

    private final ReportRepository reportRepository;

    private static final Logger log = LoggerFactory.getLogger(ManageStatusUseCaseImpl.class);

    public ManageStatusUseCaseImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void execute(ReportRequest request, ReportStatus status) {

        if (request == null || status == null) {
            throw new MessagePublishingException("Solicitação nula. Nenhuma publicação realizada.");
        }
        request.setStatus(status);
        reportRepository.save(request);
        log.info("Status da solicitação {} atualizado para {}", request.getId(), status);
    }

}
