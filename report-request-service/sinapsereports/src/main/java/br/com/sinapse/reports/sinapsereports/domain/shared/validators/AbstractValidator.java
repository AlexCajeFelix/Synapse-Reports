package br.com.sinapse.reports.sinapsereports.domain.shared.validators;

public abstract class AbstractValidator<T> {

    protected final Notification notification;
    protected final T entity;

    public AbstractValidator(T entity, Notification notification) {
        this.notification = notification;
        this.entity = entity;
    }

    public abstract void validate();

}
