package com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException;

public class SendReportToKafkaException extends RuntimeException {

    public SendReportToKafkaException(String message) {
        super(message, null, true, false);
    }

}
