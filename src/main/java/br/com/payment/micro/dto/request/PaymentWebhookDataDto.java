package br.com.payment.micro.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentWebhookDataDto(
        @NotBlank String id
) {
}
