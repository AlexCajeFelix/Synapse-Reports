package br.com.sinapse.reports.sinapsereports.Application.Enum;

import java.util.Optional;

public enum ReportType {
    PDF, CSV;

    public static Optional<ReportType> from(String reportType) {
        for (ReportType type : ReportType.values()) {
            if (type.name().equalsIgnoreCase(reportType)) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }
}
