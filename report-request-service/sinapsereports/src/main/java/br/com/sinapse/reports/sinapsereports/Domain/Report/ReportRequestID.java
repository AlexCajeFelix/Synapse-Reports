package br.com.sinapse.reports.sinapsereports.Domain.Report;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.Domain.Identifier;

public class ReportRequestID extends Identifier<UUID> {

    protected ReportRequestID(UUID value) {
        super(value);
    }

    private ReportRequestID() {
        super(UUID.randomUUID());
    }

    public static ReportRequestID createRandomID() {
        return new ReportRequestID();
    }

}
