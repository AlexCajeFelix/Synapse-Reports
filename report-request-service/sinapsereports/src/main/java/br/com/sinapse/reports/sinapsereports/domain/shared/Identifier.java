package br.com.sinapse.reports.sinapsereports.domain.shared;

public abstract class Identifier<ID> {

    private final ID value;

    protected Identifier(ID value) {
        this.value = value;
    }

    public ID getValue() {
        return value;
    }
}
