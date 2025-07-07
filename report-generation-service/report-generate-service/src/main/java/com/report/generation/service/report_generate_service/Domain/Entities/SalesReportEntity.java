package com.report.generation.service.report_generate_service.Domain.Entities;

import java.time.LocalDate;

import com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException.ProducerReportException;
import com.report.generation.service.report_generate_service.Domain.Exceptions.ValidationsRules.SalesReportValidationRules;
import com.report.generation.service.report_generate_service.Domain.Exceptions.Validators.Notification;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reports")
@AllArgsConstructor
public class SalesReportEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    @Column(name = "product")
    private String product;
    @Column(name = "sale_date")
    private LocalDate saleDate;
    @Column(name = "details")
    private String details;

    public SalesReportEntity(String product, LocalDate saleDate, String details) {
        this.product = product;
        this.saleDate = saleDate;
        this.details = details;
        validateSelf();
    }

    public void validateSelf() {
        SalesReportValidationRules validator = new SalesReportValidationRules();
        Notification<SalesReportEntity> notification = validator.validate(this);

        if (notification.hasErrors()) {
            throw new ProducerReportException(notification.messagesAsString());
        }
    }

    public SalesReportEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return id + ", product=" + product + ", saleDate=" + saleDate + ", details=" + details;
    }

}
