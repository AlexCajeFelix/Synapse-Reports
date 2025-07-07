package com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException;

public class SendReportToRabbitMqException extends RuntimeException {

    public SendReportToRabbitMqException(String message) {
        super(message, null, true, false);
    }

}
