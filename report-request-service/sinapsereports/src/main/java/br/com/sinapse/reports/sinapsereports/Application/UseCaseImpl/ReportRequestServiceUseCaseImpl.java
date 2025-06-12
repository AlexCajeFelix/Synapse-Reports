package br.com.sinapse.reports.sinapsereports.Application.UseCaseImpl;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;
import br.com.sinapse.reports.sinapsereports.Application.Mappers.ReportMapper;
import br.com.sinapse.reports.sinapsereports.Application.UseCase.ReportRequestServiceUseCase;
import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import br.com.sinapse.reports.sinapsereports.Infra.Repository.ReportRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class ReportRequestServiceUseCaseImpl implements ReportRequestServiceUseCase {

    private final ReportMapper reportMapper;
    private final StreamBridge streamBridge;
    private final ReportRepository reportRepository;

    public ReportRequestServiceUseCaseImpl(ReportMapper reportMapper, StreamBridge streamBridge,
            ReportRepository reportRepository) {
        this.reportMapper = reportMapper;
        this.streamBridge = streamBridge;
        this.reportRepository = reportRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(ReportRequestServiceUseCaseImpl.class);
    private static final String PRODUCER_BINDING_NAME = "publishReportRequest-out-0";

    @Async("reportTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ReportRequestResponseDto> requestNewReport(ReportRequest requestDto) {
        log.info("Iniciando o processamento do pedido de relatório: {}", requestDto.getId());

        CompletableFuture<ReportRequestResponseDto> future = CompletableFuture.supplyAsync(() -> {
            log.info("Salvando o pedido de relatório no banco de dados: {}", requestDto.getId());
            reportRepository.save(requestDto);
            log.info("Pedido de relatório salvo com sucesso: {}", requestDto.getId());
            log.info("enviando o pedido pro kafka : {}", requestDto.getId());

            ReportRequestResponseDto responseDto = reportMapper.toResponseDto(requestDto);

            streamBridge.send(PRODUCER_BINDING_NAME, responseDto);

            return responseDto;
        });
        return future;
    }
}
