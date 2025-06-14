package br.com.sinapse.reports.sinapsereports.Application;

import br.com.sinapse.reports.sinapsereports.Domain.Entities.ReportRequest;
import java.time.LocalDate;
import java.util.UUID;

public class ReportRequestFactory {

    private ReportRequestFactory() {
    }

    public static ReportRequest create_pdf_report_request_engineering() {
        ReportRequest request = new ReportRequest();

        request.setId(UUID.fromString("a1b2c3d4-e5f6-1111-8888-0123456789ab"));
        request.setReportType("pdf");
        request.setParameters("CURSO=ENGaENHARIA");
        request.setReportStartDate(LocalDate.parse("2024-01-01"));
        request.setReportEndDate(LocalDate.parse("2024-12-31"));

        return request;
    }

    public static ReportRequest create_csv_report_request_sales() {
        ReportRequest request = new ReportRequest();
        request.setId(UUID.fromString("b1c2d3e4-f5g6-2222-9999-9876543210ba"));
        request.setReportType("csv");
        request.setParameters("FILIAL=SP;PERIODO=ULTIMOS_30_DIAS");
        request.setReportStartDate(LocalDate.now().minusDays(30));
        request.setReportEndDate(LocalDate.now());

        return request;
    }
}
