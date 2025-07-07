package br.com.sinapse.reports.sinapsereports.domain.shared.globalexceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.sinapse.reports.sinapsereports.application.report.dtos.error.ErrorResponseDTO;
import br.com.sinapse.reports.sinapsereports.domain.report.exceptions.customexception.ReportRequestInvalidException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReportRequestInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleReportRequestInvalidException(ReportRequestInvalidException ex,
            HttpServletRequest request) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("Mensagens", List.of(ex.getMessage()));

        var uri = request.getRequestURI();

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                uri,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
