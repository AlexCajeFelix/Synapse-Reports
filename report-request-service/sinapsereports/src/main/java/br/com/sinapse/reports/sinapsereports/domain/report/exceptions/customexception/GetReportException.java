package br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception;

public class GetReportException extends RuntimeException {
    public GetReportException(String message) {
        super(message, null, true, false);

    }

}
