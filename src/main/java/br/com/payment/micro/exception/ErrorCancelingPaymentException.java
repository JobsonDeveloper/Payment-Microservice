package br.com.payment.micro.exception;

public class ErrorCancelingPaymentException extends RuntimeException {
    public ErrorCancelingPaymentException() {
        super("It was not possible to cancel the payment!");
    }

    public ErrorCancelingPaymentException(String message) {
        super(message);
    }
}
