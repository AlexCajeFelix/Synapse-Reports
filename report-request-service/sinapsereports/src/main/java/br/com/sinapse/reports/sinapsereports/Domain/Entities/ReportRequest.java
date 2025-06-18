package br.com.sinapse.reports.sinapsereports.Domain.Entities;

import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportStatus;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.ReportRequestInvalidException;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.Notification;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.Validators.ValidatorsRules.ReportRequestValidator;
import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "report_requests")
@ToString
public class ReportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "report_start_date")
    private LocalDate reportStartDate;

    @Column(name = "report_end_date")
    private LocalDate reportEndDate;

    @Column(name = "parameters")
    private String parameters;

    public ReportRequest(ReportType reportType, LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {

        this.reportType = reportType;
        this.reportStartDate = reportStartDate;
        this.reportEndDate = reportEndDate;
        this.parameters = parameters;
        this.requestedAt = LocalDateTime.now();
        validateSelf();

    }

    private void validateSelf() {
        ReportRequestValidator validator = new ReportRequestValidator();
        Notification<ReportRequest> notification = validator.validate(this);

        if (notification.hasErrors()) {
            throw new ReportRequestInvalidException(notification.messagesAsString());
        }
    }

    public ReportRequest() {
    }

    public UUID getId() {
        return id;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDate getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(LocalDate reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public LocalDate getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(LocalDate reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

}