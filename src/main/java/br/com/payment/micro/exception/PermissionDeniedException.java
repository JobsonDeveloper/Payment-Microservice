package br.com.payment.micro.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("Permission denied! Payment not linked to this user!");
    }
    public PermissionDeniedException(String message) {
        super(message);
    }
}
