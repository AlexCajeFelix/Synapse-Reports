package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators;

public abstract class AbstractValidator {

    protected final ValidatorHandler handler;

    public AbstractValidator(ValidatorHandler aHandler) {
        this.handler = aHandler;
    }

    public abstract void validate();

    protected ValidatorHandler validatorHandler() {
        return this.handler;
    }

}
