package br.com.sinapse.reports.sinapsereports.Domain.Report.Enum;

import java.util.Optional;

public enum ReportStatus {
    PENDING, FAILED, PENDING_SEND;

    public static Optional<ReportStatus> from(final String status) {
        for (ReportStatus reportStatus : ReportStatus.values()) {
            if (reportStatus.name().equalsIgnoreCase(status)) {
                return Optional.of(reportStatus);
            }
        }
        return Optional.empty();

    }
}
