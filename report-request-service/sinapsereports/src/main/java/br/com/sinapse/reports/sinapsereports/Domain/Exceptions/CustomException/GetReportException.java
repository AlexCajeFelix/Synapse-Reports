package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException;

public class GetReportException extends RuntimeException {
    public GetReportException(String message) {
        super(message, null, true, false);
    }

}
