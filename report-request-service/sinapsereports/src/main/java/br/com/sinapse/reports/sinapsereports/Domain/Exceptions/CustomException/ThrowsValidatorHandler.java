package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException;

import java.util.List;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.Error;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.Validate;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorHandler;

public class ThrowsValidatorHandler implements ValidatorHandler {

    @Override
    public ValidatorHandler append(Error anError) {
        throw ReportRequestInvalidException.create(anError);
    }

    @Override
    public ValidatorHandler append(ValidatorHandler validatorHandler) {

        throw ReportRequestInvalidException.create(validatorHandler.getErrors());
    }

    @Override
    public ValidatorHandler append(Validate validate) {
        try {
            validate.validate();
        } catch (Exception e) {
            throw ReportRequestInvalidException.create(new Error(e.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }

}
