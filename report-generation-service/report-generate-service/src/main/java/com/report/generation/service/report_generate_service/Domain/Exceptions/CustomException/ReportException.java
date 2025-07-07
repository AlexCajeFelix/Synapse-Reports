package com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException;

public class ReportException extends RuntimeException {
    public ReportException(String message) {
        super(message, null, true, false);
    }

}
