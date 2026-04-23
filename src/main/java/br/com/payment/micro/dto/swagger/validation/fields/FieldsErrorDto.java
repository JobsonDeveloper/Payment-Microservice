package br.com.payment.micro.dto.swagger.validation.fields;

import java.util.List;

public record FieldsErrorDto(
        String error,
        List<FieldErrorDetailsDto> errors
) {
}
