package br.com.payment.micro.exception;

public class PaymentAlreadyMadeException extends RuntimeException {
    public PaymentAlreadyMadeException() {super("Payment for this purchase has already been processed.");}
    public PaymentAlreadyMadeException(String message) {
        super(message);
    }
}
