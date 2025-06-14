package br.com.sinapse.reports.sinapsereports.Application.Dtos.ErrorDto;

import java.util.List;
import java.util.Map;

public class ErrorResponseDTO {

    private String instance;
    private String title;
    private int status;
    private Map<String, List<String>> errors;

    public ErrorResponseDTO() {
    }

    // Construtor completo
    public ErrorResponseDTO(String instance, String title, int status, Map<String, List<String>> errors) {
        this.instance = instance;
        this.title = title;
        this.status = status;
        this.errors = errors;
    }

    // Getters e setters
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}
