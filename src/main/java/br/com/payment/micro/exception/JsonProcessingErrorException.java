package br.com.payment.micro.exception;

public class JsonProcessingErrorException extends RuntimeException {
    public JsonProcessingErrorException(String message) {
        super(message);
    }
}
