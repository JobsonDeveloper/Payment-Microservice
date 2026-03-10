package br.com.payment.micro.exception;

public class ErrorGettingPaymentLinkException extends RuntimeException {
    public ErrorGettingPaymentLinkException() {super("It was not possible to get payment link!");}
    public ErrorGettingPaymentLinkException(String message) {
        super(message);
    }
}
