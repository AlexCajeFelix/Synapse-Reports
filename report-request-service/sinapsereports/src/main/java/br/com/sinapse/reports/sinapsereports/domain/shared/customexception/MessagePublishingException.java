package br.com.sinapse.reports.sinapsereports.domain.shared.customexception;

import java.util.List;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Error;

public class MessagePublishingException extends NoStackTraceException {

    private final List<Error> errors;

    private MessagePublishingException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static MessagePublishingException create(final Error error) {
        return new MessagePublishingException(error.getError(), List.of(error));
    }

    public static MessagePublishingException create(final List<Error> errors) {
        String message = (errors != null && !errors.isEmpty())
                ? errors.stream()
                        .map(Error::getError)
                        .reduce((first, second) -> first + ", " + second)
                        .orElse("Erro de validação")
                : "Erro de validação";
        return new MessagePublishingException("Falha na validação dos atributos: " + message, errors);
    }

    public List<Error> getErrors() {
        return errors;
    }

}
