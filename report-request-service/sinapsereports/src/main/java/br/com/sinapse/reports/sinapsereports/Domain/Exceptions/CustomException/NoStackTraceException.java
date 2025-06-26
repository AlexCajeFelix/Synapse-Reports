package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException;

public class NoStackTraceException extends RuntimeException {

    protected NoStackTraceException(final String message) {
        super(message, null);
    }

    protected NoStackTraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
