package com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException;

public class ProducerReportException extends RuntimeException {

    public ProducerReportException(String message) {
        super(message, null, true, false);
    }
}
