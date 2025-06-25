package br.com.sinapse.reports.sinapsereports.Domain;

import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorHandler;

public abstract class Entity<ID extends Identifier<?>> {

    protected final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    protected ID getId() {
        return id;
    }

    public abstract void validate(ValidatorHandler handler);
}
