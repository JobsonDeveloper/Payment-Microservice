package br.com.payment.micro.exception;

public class PaymentNotFoundException extends RuntimeException {
  public PaymentNotFoundException() {
    super("Payment not found!");
  }
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
