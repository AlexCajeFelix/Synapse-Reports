package br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception;

import java.util.List;

import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Error;

public class ReportRequestInvalidException extends NoStackTraceException {

    private final List<Error> errors;

    private ReportRequestInvalidException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static ReportRequestInvalidException create(final Error error) {
        return new ReportRequestInvalidException(error.getError(), List.of(error));
    }

    public static ReportRequestInvalidException create(final List<Error> errors) {
        final String message = (errors != null && !errors.isEmpty())
                ? errors.get(0).getError()
                : "Erro de validação";
        return new ReportRequestInvalidException(message, errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
