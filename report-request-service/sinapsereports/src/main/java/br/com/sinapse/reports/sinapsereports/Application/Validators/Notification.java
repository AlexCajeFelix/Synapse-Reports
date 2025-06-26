package br.com.sinapse.reports.sinapsereports.Application.Validators;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.Error;

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
}
