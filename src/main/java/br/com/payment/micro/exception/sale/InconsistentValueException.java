package br.com.payment.micro.exception.sale;

public class InconsistentValueException extends RuntimeException {
    public InconsistentValueException() {
        super("The amount to be paid is inconsistent!");
    }
    public InconsistentValueException(String message) {
        super(message);
    }
}
