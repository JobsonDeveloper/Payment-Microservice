package br.com.payment.micro.exception.sale;

public class ErrorRetrievingSaleInfoException extends RuntimeException {
    public ErrorRetrievingSaleInfoException() {
        super("It was not possible to get sale info!");
    }
    public ErrorRetrievingSaleInfoException(String message) {
        super();
    }
}
