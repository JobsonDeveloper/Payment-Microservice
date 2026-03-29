package br.com.payment.micro.exception.mercadoPago;

public class ErrorGettingExternalPaymentDetailsException extends RuntimeException {
    public ErrorGettingExternalPaymentDetailsException(String message) {
        super(message);
    }
}
