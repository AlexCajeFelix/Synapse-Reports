package br.com.sinapse.reports.sinapsereports.domain.report;

import java.time.Instant;
import java.time.LocalDate;

import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportStatus;
import br.com.sinapse.reports.sinapsereports.domain.report.enums.ReportType;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception.ReportRequestInvalidException;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception.ThrowsValidatorHandler;
import br.com.sinapse.reports.sinapsereports.domain.shared.AgregateRoot;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.Error;
import br.com.sinapse.reports.sinapsereports.domain.shared.validators.ValidatorHandler;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.validators.validatorrules.RequestReportValidator;

public class ReportRequest extends AgregateRoot<ReportRequestID> {

    private ReportStatus status;

    private ReportType reportType;

    private Instant requestedAt;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

    private String parameters;

    private ReportRequest(ReportRequestID id, ReportStatus status, ReportType reportType, Instant requestedAt,
            LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {
        super(id);
        this.status = status;
        this.reportType = reportType;
        this.requestedAt = requestedAt;
        this.reportStartDate = reportStartDate;
        this.reportEndDate = reportEndDate;
        this.parameters = parameters;
    }

    public static ReportRequest with(
            ReportRequestID id,
            ReportStatus status,
            ReportType reportType,
            Instant requestedAt,
            LocalDate reportStartDate,
            LocalDate reportEndDate,
            String parameters) {

        return new ReportRequest(
                id,
                status,
                reportType,
                requestedAt,
                reportStartDate,
                reportEndDate,
                parameters);
    }

    private ReportRequest(ReportStatus status, ReportType reportType,
            LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {
        super(ReportRequestID.createRandomID());
        this.status = status;
        this.reportType = reportType;
        this.requestedAt = Instant.now();
        this.reportStartDate = reportStartDate;
        this.reportEndDate = reportEndDate;
        this.parameters = parameters;
    }

    public static ReportRequest create(String status, String reportType,
            LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {
        var reportRequest = new ReportRequest(
                ReportStatus.from(status).orElseThrow(
                        () -> ReportRequestInvalidException.create(new Error("Status do relatorio invalido"))),
                ReportType.from(reportType)
                        .orElseThrow(
                                () -> ReportRequestInvalidException.create(new Error("Tipo do relatorio invalido"))),
                reportStartDate,
                reportEndDate,
                parameters);

        reportRequest.validate(new ThrowsValidatorHandler());
        return reportRequest;

    }

    public ReportRequestID getId() {
        return id;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public LocalDate getReportStartDate() {
        return reportStartDate;
    }

    public LocalDate getReportEndDate() {
        return reportEndDate;
    }

    public String getParameters() {
        return parameters;
    }

    @Override
    public void validate(ValidatorHandler handler) {
        new RequestReportValidator(handler, this).validate();
    }

    public void updateStatus(String status) {
        this.status = ReportStatus.from(status)
                .orElseThrow(() -> ReportRequestInvalidException.create(new Error("Status do relatorio invalido")));
    }

}