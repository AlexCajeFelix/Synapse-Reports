package com.report.generation.service.report_generate_service.Application.Dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDTO {
    private String product;
    private LocalDate saleDate;
    private String details;

    // construtor, getters/setters
}