package br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception;

public class MessagePublishingException extends RuntimeException {
    public MessagePublishingException(String message) {
        super(message);
    }
}
