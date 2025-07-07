package br.com.sinapse.reports.sinapsereports.domain.report;

import java.util.UUID;

import br.com.sinapse.reports.sinapsereports.domain.shared.Identifier;

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

    public static ReportRequestID from(UUID id) {
        return new ReportRequestID(id);
    }

}
