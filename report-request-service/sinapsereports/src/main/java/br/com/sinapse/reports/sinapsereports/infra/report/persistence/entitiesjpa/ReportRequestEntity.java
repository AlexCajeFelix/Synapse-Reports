package br.com.sinapse.reports.sinapsereports.infra.report.persistence.entitiesjpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "report_requests")
@Getter
@Setter
public class ReportRequestEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "report_type", nullable = false, length = 100)
    private String reportType;
    @Column(name = "report_start_date", nullable = false, columnDefinition = "DATE")
    private LocalDate reportStartDate;

    @Column(name = "report_end_date", nullable = false, columnDefinition = "DATE")
    private LocalDate reportEndDate;

    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    @Column(name = "requested_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant requestedAt;

    public ReportRequestEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }

}
