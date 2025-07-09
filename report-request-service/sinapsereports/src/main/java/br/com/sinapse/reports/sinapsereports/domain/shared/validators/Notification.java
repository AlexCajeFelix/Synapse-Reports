package br.com.sinapse.reports.sinapsereports.domain.shared.validators;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private final List<Error> errors = new ArrayList<>();

    public void append(Error error) {
        this.errors.add(error);
    }

    public Notification append(Notification notification) {
        this.errors.addAll(notification.getErrors());
        return this;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public static Notification create() {
        return new Notification();
    }

    public String messagesAsString() {
        return errors.stream()
                .map(Error::getError)
                .reduce((first, second) -> first + ", " + second)
                .orElse("No errors");
    }
}