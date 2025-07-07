package com.report.generation.service.report_generate_service.Application.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.report.generation.service.report_generate_service.Application.Dtos.SalesReportDTO;
import com.report.generation.service.report_generate_service.Domain.Entities.SalesReportEntity;

@Service
public class SalesReportMapper {

    public static SalesReportDTO toDTO(SalesReportEntity entity) {
        return new SalesReportDTO(
                entity.getProduct(),
                entity.getSaleDate(),
                entity.getDetails());
    }

    public static List<SalesReportDTO> toDTOList(List<SalesReportEntity> entities) {
        return entities.stream()
                .map(SalesReportMapper::toDTO)
                .collect(Collectors.toList());
    }
}
