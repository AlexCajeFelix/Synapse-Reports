package br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators;

import java.util.List;

public interface ValidatorHandler {

    ValidatorHandler append(Error anError);

    ValidatorHandler append(ValidatorHandler validatorHandler);

    ValidatorHandler append(Validate validate);

    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors().size() > 0 && getErrors() != null && !getErrors().isEmpty();
    }
}
