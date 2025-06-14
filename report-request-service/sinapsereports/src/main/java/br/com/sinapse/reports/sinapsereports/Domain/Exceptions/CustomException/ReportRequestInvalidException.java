package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException;

public class ReportRequestInvalidException extends RuntimeException {
    public ReportRequestInvalidException(String message) {
        super(message, null, true, false);
    }
}
