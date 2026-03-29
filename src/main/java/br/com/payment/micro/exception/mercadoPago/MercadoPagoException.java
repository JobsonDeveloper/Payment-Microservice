package br.com.payment.micro.exception.mercadoPago;

public class MercadoPagoException extends RuntimeException {
    public MercadoPagoException(String message) {
        super(message);
    }
}
