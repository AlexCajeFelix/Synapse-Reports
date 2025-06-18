package com.report.generation.service.report_generate_service.Application.UseCaseImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.report.generation.service.report_generate_service.Application.Enum.ReportStatus;
import com.report.generation.service.report_generate_service.Application.UseCase.GenereteReportUseCase;
import com.report.generation.service.report_generate_service.Domain.Entities.Report;
import com.report.generation.service.report_generate_service.Domain.Entities.SalesReportEntity;
import com.report.generation.service.report_generate_service.Infra.Repository.ReportRepository;

@Service
public class GenereteReposrtUseCaseImpl extends GenereteReportUseCase {

    @Autowired
    private ReportRepository reportRepository;

    @Value("${reports.storage.path}")
    private String storagePath;

    @Value("${download-service.url}")
    private String downloadServiceUrl;

    private ProducerReportUseCaseImpl producerReportUseCaseImpl;

    public GenereteReposrtUseCaseImpl(ProducerReportUseCaseImpl producerReportUseCaseImpl) {
        this.producerReportUseCaseImpl = producerReportUseCaseImpl;
    }

    private final String reportNameFile = "relatorio_";

    @Override
    public void generateReport(Report report) {

        String fileName = getDownloadsFolder(report);

        Path filePath = Paths.get(storagePath, fileName);

        List<SalesReportEntity> reports = getReports(report);

        writerFile(filePath, reports);

        String finalDownloadLink = downloadServiceUrl + "/" + fileName;

        report.setStatus(ReportStatus.COMPLETED);
        report.setLinkDownload(finalDownloadLink);

        System.out.println("Status da solicitação " + report.getId() + " atualizado para " + ReportStatus.COMPLETED);

        producerReportUseCaseImpl.sendReportToKafka(report);

        System.out.println("Relatório enviado para o Kafka");

        producerReportUseCaseImpl.sendReportToRabbitMQ(report);

        System.out.println("enviado o link com o id pro RABBIT MQ");

    }

    public void writerFile(Path filePath, List<SalesReportEntity> reports) {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            for (SalesReportEntity r : reports) {
                writer.write(r.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao escrever o arquivo de relatório", e);
        }
    }

    public List<SalesReportEntity> getReports(Report report) {
        List<SalesReportEntity> reports = reportRepository.findByDateRange(
                report.getReportStartDate(), report.getReportEndDate());

        System.out.println("Qtd de registros encontrados: " + reports.size());
        return reports;
    }

    public String getDownloadsFolder(Report report) {
        String fileName = reportNameFile + System.currentTimeMillis() + "."
                + report.getReportType().name().toLowerCase();

        File downloadsFolder = new File(storagePath);
        if (!downloadsFolder.exists()) {
            boolean created = downloadsFolder.mkdirs();
            if (!created) {
                throw new RuntimeException("Não foi possível criar o diretório: " + storagePath);
            }
        }
        return fileName;
    }

}
