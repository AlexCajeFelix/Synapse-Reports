package com.report.generation.service.report_generate_service.Domain.Exceptions.Validators;

public interface Validator<T> {
    Notification<T> validate(T target);
}
