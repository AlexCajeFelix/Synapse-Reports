package br.com.sinapse.reports.sinapsereports.Domain.Report;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.AgregateRoot;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorHandler;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorsRules.RequestReportValidator;

import java.time.Instant;
import java.time.LocalDate;

public class ReportRequest extends AgregateRoot<ReportRequestID> {

    private ReportStatus status;

    private ReportType reportType;

    private Instant requestedAt;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

    private String parameters;

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

    public static ReportRequest create(ReportStatus status, ReportType reportType,
            LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {
        return new ReportRequest(status, reportType, reportStartDate, reportEndDate, parameters);
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

}