package br.com.sinapse.reports.sinapsereports.Factory;

import java.time.LocalDate;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.CreateReportRequestDto;
import br.com.sinapse.reports.sinapsereports.Application.Enum.ReportType;
import br.com.sinapse.reports.sinapsereports.Domain.Exceptions.CustomException.ReportRequestInvalidException;

public class CreateRequestDtoFactory {

        private static CreateReportRequestDto genereteDTO(String reportType,
                        LocalDate reportStartDate, LocalDate reportEndDate, String parameters) {
                var reportTypeEnum = ReportType.from(reportType)
                                .orElseThrow(() -> new ReportRequestInvalidException("Report type must not be null."));
                return new CreateReportRequestDto(
                                reportTypeEnum.name(),
                                LocalDate.now(),
                                LocalDate.now(),
                                parameters);
        }

        public static CreateReportRequestDto createValidCreateReportRequestDto() {
                return genereteDTO(
                                "pdf",
                                LocalDate.now(),
                                LocalDate.now(),
                                "parameters");
        }

        public static CreateReportRequestDto createInvalidCreateReportRequestDto() {
                return genereteDTO(
                                "",
                                LocalDate.now(),
                                LocalDate.now(),
                                "parameters");
        }

}
