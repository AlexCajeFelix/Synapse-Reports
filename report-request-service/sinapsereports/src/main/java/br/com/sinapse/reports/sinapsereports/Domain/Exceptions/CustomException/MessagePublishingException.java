package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException;

public class MessagePublishingException extends RuntimeException {
    public MessagePublishingException(String message) {
        super(message);
    }
}
