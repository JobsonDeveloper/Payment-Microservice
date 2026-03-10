package br.com.payment.micro.exception.sale;

public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException() {super("Sale not found!");}
    public SaleNotFoundException(String message) {
        super(message);
    }
}
