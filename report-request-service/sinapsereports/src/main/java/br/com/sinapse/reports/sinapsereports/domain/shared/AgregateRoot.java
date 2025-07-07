package br.com.sinapse.reports.sinapsereports.domain.shared;

public abstract class AgregateRoot<ID extends Identifier<?>> extends Entity<ID> {

    public AgregateRoot(ID id) {
        super(id);
    }

}
