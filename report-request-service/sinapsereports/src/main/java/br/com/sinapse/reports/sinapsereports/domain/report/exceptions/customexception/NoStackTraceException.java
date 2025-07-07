package br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception;

public class NoStackTraceException extends RuntimeException {

    protected NoStackTraceException(final String message) {
        super(message, null);
    }

    protected NoStackTraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
