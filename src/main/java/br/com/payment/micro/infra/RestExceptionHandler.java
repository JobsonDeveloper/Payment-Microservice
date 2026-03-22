package br.com.payment.micro.infra;

import br.com.payment.micro.exception.*;
import br.com.payment.micro.exception.sale.ErrorRetrievingSaleInfoException;
import br.com.payment.micro.exception.sale.SaleNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private Map<String, String> mapError(FieldError fieldError) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("field", fieldError.getField());
        mapping.put("message", fieldError.getDefaultMessage());

        return mapping;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");

        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapError)
                .toList();
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ErrorGettingPaymentLinkException.class)
    private ResponseEntity<DefaultErrorResponse> errorGettingPaymentLinkHandler(ErrorGettingPaymentLinkException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(defaultErrorResponse);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    private ResponseEntity<DefaultErrorResponse> serviceUnavailableHandler(ServiceUnavailableException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(defaultErrorResponse);
    }

    @ExceptionHandler(SaleNotFoundException.class)
    private ResponseEntity<DefaultErrorResponse> saleNotFoundHandler(SaleNotFoundException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(defaultErrorResponse);
    }

    @ExceptionHandler(ErrorRetrievingSaleInfoException.class)
    private ResponseEntity<DefaultErrorResponse> errorRetrievingSaleInfoHandler(ErrorRetrievingSaleInfoException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResponse);
    }

    @ExceptionHandler(ErrorChangingPaymentStatusException.class)
    private ResponseEntity<DefaultErrorResponse> errorChangingPaymentStatusHandler(ErrorChangingPaymentStatusException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResponse);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    private ResponseEntity<DefaultErrorResponse> paymentNotFoundHandler(PaymentNotFoundException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(defaultErrorResponse);
    }

    @ExceptionHandler(ErrorDeletingPaymentException.class)
    private ResponseEntity<DefaultErrorResponse> errorDeletingPaymentHandler(ErrorDeletingPaymentException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResponse);
    }

    @ExceptionHandler(ErrorCancelingPaymentException.class)
    private ResponseEntity<DefaultErrorResponse> errorCancelingPaymentHandler(ErrorCancelingPaymentException exception) {
        DefaultErrorResponse defaultErrorResponse = new DefaultErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResponse);
    }
}
