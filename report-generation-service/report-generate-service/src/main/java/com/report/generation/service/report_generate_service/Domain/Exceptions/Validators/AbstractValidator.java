package com.report.generation.service.report_generate_service.Domain.Exceptions.Validators;

public abstract class AbstractValidator<T> implements Validator<T> {

    protected Notification<T> notification;

    protected AbstractValidator() {
        this.notification = new Notification<>();
    }

    protected void addError(String message) {
        this.notification.addError(message);
    }

    @Override
    public Notification<T> validate(T target) {
        this.notification = new Notification<>();
        this.doValidate(target);
        return notification;
    }

    protected abstract void doValidate(T target);
}
