package com.report.generation.service.report_generate_service.Domain.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.report.generation.service.report_generate_service.Application.Enum.ReportStatus;
import com.report.generation.service.report_generate_service.Application.Enum.ReportType;
import com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException.ReportException;
import com.report.generation.service.report_generate_service.Domain.Exceptions.ValidationsRules.ReportValidationRules;
import com.report.generation.service.report_generate_service.Domain.Exceptions.Validators.Notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Report {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    private ReportType reportType;

    private ReportStatus status;

    private LocalDate reportStartDate;

    private LocalDate reportEndDate;

    private LocalDateTime requestedAt;

    private String parameters;

    private String linkDownload;

    public Report(ReportType reportType, LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {
        this.reportType = reportType;
        this.reportStartDate = reportStartDate;
        this.reportEndDate = reportEndDate;
        this.parameters = parameters;
        this.status = ReportStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
        validationSelf();
    }

    public void validationSelf() {

        ReportValidationRules reportValidationRules = new ReportValidationRules();

        Notification<Report> notification = reportValidationRules.validate(this);

        if (notification.hasErrors()) {
            throw new ReportException(notification.messagesAsString());
        }
    }

    public void updateStatus(ReportStatus status) {
        this.status = status;
    }

    public void updateLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    public void updateReportEndDate(LocalDate reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

    public void updateReportStartDate(LocalDate reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public void updateReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public void updateParameters(String parameters) {
        this.parameters = parameters;
    }

    public void updateRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void updateId(UUID id) {
        this.id = id;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDate getReportStartDate() {
        return reportStartDate;
    }

    public LocalDate getReportEndDate() {
        return reportEndDate;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public String getParameters() {
        return parameters;
    }

    public String getLinkDownload() {
        return linkDownload;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setReportStartDate(LocalDate reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public void setReportEndDate(LocalDate reportEndDate) {
        this.reportEndDate = reportEndDate;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

}
