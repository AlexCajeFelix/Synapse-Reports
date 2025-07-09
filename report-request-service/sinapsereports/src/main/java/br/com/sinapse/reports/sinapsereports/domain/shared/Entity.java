package br.com.sinapse.reports.sinapsereports.domain.shared;

public abstract class Entity<ID extends Identifier<?>> {

    protected final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    protected ID getId() {
        return id;
    }

    public abstract void validate();
}
