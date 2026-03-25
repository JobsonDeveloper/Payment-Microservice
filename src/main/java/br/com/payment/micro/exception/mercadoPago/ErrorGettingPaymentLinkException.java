package br.com.payment.micro.exception.mercadoPago;

public class ErrorGettingPaymentLinkException extends RuntimeException {
    public ErrorGettingPaymentLinkException(String message) {
        super(message);
    }
}
