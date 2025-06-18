package com.report.generation.service.report_generate_service.Application.UseCaseImpl;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.report.generation.service.report_generate_service.Application.UseCase.ProducerReportUseCase;
import com.report.generation.service.report_generate_service.Domain.Entities.Report;

@Service
public class ProducerReportUseCaseImpl extends ProducerReportUseCase {

    private final StreamBridge streamBridge;

    public ProducerReportUseCaseImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendReportToKafka(Report report) {
        streamBridge.send("produceReport-out-0", report);
    }

    public void sendReportToRabbitMQ(Report report) {
        streamBridge.send("enviarlinkdownload", report);
    }

}
