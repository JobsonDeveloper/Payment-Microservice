package br.com.payment.micro.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String service) {
        super("Service '" + service + "' is unavailable!");
    }
}
