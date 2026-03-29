package br.com.payment.micro.exception;

public class ErrorChangingPaymentStatusException extends RuntimeException {
    public ErrorChangingPaymentStatusException() {super("It was not possible to update the payment status!");}
    public ErrorChangingPaymentStatusException(String message) {
        super(message);
    }
}
