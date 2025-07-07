package br.com.sinapse.reports.sinapsereports.domain.shared.validators;

public class Error {

    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getError() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
