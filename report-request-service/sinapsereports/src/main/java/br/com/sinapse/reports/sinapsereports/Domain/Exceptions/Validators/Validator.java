package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators;

public interface Validator<T> {
    Notification<T> validate(T target);
}
