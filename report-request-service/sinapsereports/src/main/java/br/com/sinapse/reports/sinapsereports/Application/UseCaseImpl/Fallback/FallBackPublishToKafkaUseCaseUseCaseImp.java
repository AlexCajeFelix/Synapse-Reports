package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.Fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl.ManageStatusUseCaseImpl;
import br.com.sinapse.reports.sinapsereports.Domain.Report.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Domain.Report.Enum.ReportStatus;

@Component
public class FallBackPublishToKafkaUseCaseUseCaseImp {

    private final ManageStatusUseCaseImpl manageStatusUseCase;
    private final ReportStatus FAILED = ReportStatus.FAILED;

    private static final Logger log = LoggerFactory.getLogger(FallBackPublishToKafkaUseCaseUseCaseImp.class);

    public FallBackPublishToKafkaUseCaseUseCaseImp(final ManageStatusUseCaseImpl manageStatusUseCase) {
        this.manageStatusUseCase = manageStatusUseCase;

    }

    public void execute(final ReportRequest aReport) {
        log.info("Fallback salvando a info no banco de dados como pendente de envio para a solicitação {} ",
                aReport.getId());
        manageStatusUseCase.execute(aReport, FAILED);
    }

}
