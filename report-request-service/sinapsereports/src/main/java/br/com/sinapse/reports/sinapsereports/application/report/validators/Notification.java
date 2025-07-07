package br.com.sinapse.reports.sinapsereports.application.report.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Error;

public class Notification<T> {

    private List<Error> anErrors = new ArrayList<>();

    public Notification() {
    }

    public Notification(List<Error> anErrors) {
        this.anErrors = anErrors;
    }

    public void addError(String message) {
        this.anErrors.add(new Error(message));
    }

    public List<Error> getErrors() {
        return anErrors;
    }

    public boolean hasErrors() {
        return !anErrors.isEmpty();
    }

    public String messagesAsString() {
        return anErrors.stream()
                .map(Error::getError)
                .collect(Collectors.joining("; "));
    }

    public void addErrors(List<Error> errors) {
        this.anErrors.addAll(errors);
    }
}
