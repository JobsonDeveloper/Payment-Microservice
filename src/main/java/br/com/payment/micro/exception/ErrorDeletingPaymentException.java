package br.com.payment.micro.exception;

public class ErrorDeletingPaymentException extends RuntimeException {
    public ErrorDeletingPaymentException() {
        super("It was not possible to delete the payment!");
    }
    public ErrorDeletingPaymentException(String message) {
        super(message);
    }
}
