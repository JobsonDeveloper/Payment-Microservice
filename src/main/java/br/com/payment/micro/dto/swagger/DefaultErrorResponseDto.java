package br.com.payment.micro.dto.swagger;

public record DefaultErrorResponseDto(
        String status,
        String message
) {
}
